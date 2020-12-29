package com.turing.b2c.sellergoods;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.turing.b2c.mapper.SpecificationOptionMapper;
import com.turing.b2c.mapper.TypeTemplateMapper;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.*;
import com.turing.b2c.model.pojo.union.SpecUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TypeTemplateServiceImpl implements  TypeTemplateService {

    @Autowired
    private TypeTemplateMapper typeTemplateMapper;
    @Autowired
    private SpecificationOptionMapper  specificationOptionMapper;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Override
    public TypeTemplate findById(Long id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TypeTemplate> findAll() {
        return typeTemplateMapper.selectByExample(null);
    }

    @Override
    public void save(TypeTemplate entity) {
        typeTemplateMapper.insert(entity);
    }

    @Override
    public void update(TypeTemplate entity) {
        typeTemplateMapper.updateByPrimaryKey(entity);
    }

    @Override
    public void delete(Long id) {
        typeTemplateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        TypeTemplateExample example=new TypeTemplateExample();
        //把数组转换为list
        example.createCriteria().andIdIn(Arrays.asList(ids));
        typeTemplateMapper.deleteByExample(example);
    }

    @Override
    public SearchResult<TypeTemplate> findPage(SearchParam searchParam) {
        //使用我们的分页插件
        PageHelper.startPage(searchParam.getPageNum(),searchParam.getPageSize());
        Page<TypeTemplate> page=(Page<TypeTemplate>) typeTemplateMapper.selectByExample(null);
        // CRUD类型模板的时候，都会路由该方法，因此定义在这里比较合适
        // 缓存品牌结构：key-模板id：value-品牌列表
        // 缓存规格结构：key-模板id：value-规格列表
        cacheBrandListAndSpecUnionList();
        return new SearchResult(page.getTotal(),page.getResult());
    }
    private void cacheBrandListAndSpecUnionList(){
        List<TypeTemplate> list = findAll();
        list.forEach(typeTemplate -> {
            //缓存品牌列表
            List<Brand> brandList = JSON.parseArray(typeTemplate.getBrandIds(),Brand.class);
            redisTemplate.opsForHash().put("brandList",typeTemplate.getId(),brandList);
            //缓存规格列表
            List<SpecUnion> specUnionList = findSpecUnionsById(typeTemplate.getId());
            redisTemplate.opsForHash().put("specUnionList",typeTemplate.getId(),specUnionList);
        });
    }
    @Override
    public List<SpecUnion> findSpecUnionsById(Long id) {
        List<SpecUnion> specUionList=new ArrayList<>();
        //根据id的 查询模板对象
        TypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
        String specIds=typeTemplate.getSpecIds();
        List<Specification> specifications = JSONArray.parseArray(specIds, Specification.class);
        //循环规格
        specifications.forEach(spec -> {
            SpecificationOptionExample ex=new SpecificationOptionExample();
            ex.createCriteria().andSpecIdEqualTo(spec.getId());
            List<SpecificationOption> specificationOptions = specificationOptionMapper.selectByExample(ex);
            specUionList.add(new SpecUnion(spec,specificationOptions));
        });
        return specUionList;
    }
}
