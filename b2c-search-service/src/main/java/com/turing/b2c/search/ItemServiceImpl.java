package com.turing.b2c.search;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.turing.b2c.mapper.ItemMapper;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Brand;
import com.turing.b2c.model.pojo.Item;
import com.turing.b2c.model.pojo.ItemExample;
import com.turing.b2c.model.pojo.union.SpecUnion;
import com.turing.b2c.repository.ItemRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private String[] fieldNames = new String[]{"title", "seller", "category", "brand"};

    @Override
    public Item findById(Long id) {
        return itemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Item> findAll() {
        return itemMapper.selectByExample(null);
    }

    @Override
    public void save(Item entity) {
        itemMapper.insert(entity);
    }

    @Override
    public void update(Item entity) {
        itemMapper.updateByPrimaryKey(entity);
    }

    @Override
    public void delete(Long id) {
        itemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        ItemExample example = new ItemExample();
        //把数组转换为list
        example.createCriteria().andIdIn(Arrays.asList(ids));
        itemMapper.deleteByExample(example);
    }

    @Override
    public SearchResult<Item> findPage(SearchParam searchParam) {
        //使用我们的分页插件
        SearchResult<Item> result = new SearchResult<>();
        //查询商品列表
        searchItemList(result,searchParam);
        //查询商品分类列表
        searchCategoryList(result,searchParam);
        // 查询品牌和规格列表
        String category = null;
        if (!Strings.isNullOrEmpty(searchParam.getCategory())) {
            // 如果前台提交查询条件，以前台提交为准
            category = searchParam.getCategory();
        } else if (result.getCategoryList().size() > 0) {
            // 如前台没有提交，则默认取商品类型第一个
            category = result.getCategoryList().get(0);
        }
        if (category==null){
            category = "";
        }
        //1.从缓存中获取类型ID
        Long typeTemplateId = (Long) redisTemplate.opsForHash().get("itemCat",category);
        if (typeTemplateId != null) {
            //2.从缓存中获取品牌列表
            List<Brand> brandList = (List<Brand>)redisTemplate.opsForHash().get("brandList",typeTemplateId);
            //3.从缓存中获取规格列表
            List<SpecUnion> specUnionList = (List<SpecUnion>)redisTemplate.opsForHash().get("specUnionList",typeTemplateId);
            //4.把缓存中获取的集合放到result中
            result.setBrandList(brandList);
            result.setSpecUnionList(specUnionList);
        }
        return result;
    }

    private void searchCategoryList(SearchResult<Item> result, SearchParam param) {
        //本地查询构建者
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        //索引
        searchQueryBuilder.withIndices("b2c");
        //类型
        searchQueryBuilder.withTypes("item");
        //查询所有
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        //if(param.getKeyword()!=null && param.getKeyword()!=""){}
        if (!Strings.isNullOrEmpty(param.getKeyword())) {
            //根据关键词进行查询
            queryBuilder = QueryBuilders.multiMatchQuery(param.getKeyword(), fieldNames);
        }
        //查询
        searchQueryBuilder.withQuery(queryBuilder);

        //组装聚合条件
        TermsAggregationBuilder groupbyCategory = AggregationBuilders.terms("groupbyCategory").field("category.keyword");
        NativeSearchQueryBuilder searchQueryBuilder1 = searchQueryBuilder.addAggregation(groupbyCategory);

        //build
        NativeSearchQuery searchQuery = searchQueryBuilder1.build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {

            @Override
            public Aggregations extract(SearchResponse searchResponse) {
                return searchResponse.getAggregations();
            }
        });
        //aggregations 转换为map
        Map<String, Aggregation> map = aggregations.asMap();
        //通过map的key获取StringTerms对象
        StringTerms terms = (StringTerms) map.get("groupbyCategory");
        //terms  ---buckets
        List<StringTerms.Bucket> buckets = terms.getBuckets();
        List<String> categoryList = new ArrayList<>();
        for (int i = 0; i < buckets.size(); i++) {
            //才获取到类型放入集合中
            categoryList.add(buckets.get(i).getKeyAsString());
        }
        result.setCategoryList(categoryList);

    }

    private void searchItemList(SearchResult<Item> result, SearchParam param) {
        //本地查询构建者
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        //索引
        searchQueryBuilder.withIndices("b2c");
        //类型
        searchQueryBuilder.withTypes("item");
        //查询所有
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        //if(param.getKeyword()!=null && param.getKeyword()!=""){}
        if (!Strings.isNullOrEmpty(param.getKeyword())) {
            //根据关键词进行查询
            queryBuilder = QueryBuilders.multiMatchQuery(param.getKeyword(), fieldNames);
        }
        //查询
        searchQueryBuilder.withQuery(queryBuilder);
        //添加过滤条件
        addFilter(param,searchQueryBuilder);
        //高亮显示
        // 高亮字段
        Field[] fields = createHighlightFields(fieldNames);

        searchQueryBuilder.withHighlightFields(fields);


        //分页
        searchQueryBuilder.withPageable(PageRequest.of(param.getPageNum() - 1, param.getPageSize()));

        //构建
        NativeSearchQuery searchQuery = searchQueryBuilder.build();
        AggregatedPage<Item> items = null;
        if (searchQuery!=null){
            items = elasticsearchTemplate.queryForPage(searchQuery, Item.class, searchResultMapper);
        }

        if (items!=null){
            //设置总数
            result.setTotal(items.getTotalElements());
            //设置查询集合
            result.setList(items.getContent());
        }
    }
    @SuppressWarnings("unchecked")
    private void addFilter(SearchParam param, NativeSearchQueryBuilder searchQueryBuilder) {
        //创建一个过滤构建者对象
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //设置过滤条件 交给构建者
        //1.设置分类过滤器
        if (!Strings.isNullOrEmpty(param.getCategory())){
            TermQueryBuilder term1 = QueryBuilders.termQuery("category.keyword", param.getCategory());
            queryBuilder.filter(term1);
        }
        //2.设置品牌过滤器
        if (!Strings.isNullOrEmpty(param.getBrand())){
            TermQueryBuilder term2 = QueryBuilders.termQuery("brand.keyword", param.getBrand());
            queryBuilder.filter(term2);
        }
        //3.设置规格过滤器
        Map<String,String> map = JSON.parseObject(param.getSpec(),Map.class);
        map.forEach((key,value)->{
            if (!Strings.isNullOrEmpty(value)){
                TermQueryBuilder term3 = QueryBuilders.termQuery("specMap." + key + ".keyword", value);
                queryBuilder.filter(term3);
            }
        });
        //指定过滤构建者
        if (queryBuilder.filter().size()>0){
            searchQueryBuilder.withFilter(queryBuilder);
        }
    }

    private Field[] createHighlightFields(String[] fieldNames) {
        String preTag = "<em style='color:red'>";
        String postTag = "</em>";
        Field[] fields = new Field[fieldNames.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Field(fieldNames[i]).preTags(preTag).postTags(postTag);
        }
        return fields;
    }


    private SearchResultMapper searchResultMapper = new SearchResultMapper() {

        @SuppressWarnings("unchecked")
        @Override
        public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
            List<Item> list = new ArrayList<>();
            if (response.getHits().getHits().length <= 0) {
                return null;
            }
            for (SearchHit searchHit : response.getHits()) {
                String json = searchHit.getSourceAsString();
                // 复制所有属性值到item
                Item item = JSON.parseObject(json, Item.class);
                Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                // 设置高亮属性值
                setHighlightFields(item, highlightFields);
                list.add(item);
            }
            return new AggregatedPageImpl<T>((List<T>) list, pageable, response.getHits().getTotalHits());
        }

        @Override
        public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
            return null;
        }

    };

    private void setHighlightFields(Item item, Map<String, HighlightField> highlightFields) {
        if (highlightFields.get("title") != null) {
            item.setTitle(highlightFields.get("title").getFragments()[0].toString());
        }
        if (highlightFields.get("brand") != null) {
            item.setBrand(highlightFields.get("brand").getFragments()[0].toString());
        }
        if (highlightFields.get("seller") != null) {
            item.setSeller(highlightFields.get("seller").getFragments()[0].toString());
        }
        if (highlightFields.get("category") != null) {
            item.setCategory(highlightFields.get("category").getFragments()[0].toString());
        }
    }

    @Override
    public void index() {
        //先把item中所有的数据查询出来
        List<Item> items = itemMapper.selectByExample(null);
        //循环添加到ES中
        items.forEach(item -> {
            item.setSpecMap(JSON.parseObject(item.getSpec(), Map.class));
            itemRepository.index(item);
        });
    }

    @RabbitListener(queues = "item.update")
    public void updateIndex(String json){
        //把json字符串转换为对象
        List<Item> items = JSON.parseArray(json, Item.class);
        //保存
        itemRepository.saveAll(items);
    }

}
