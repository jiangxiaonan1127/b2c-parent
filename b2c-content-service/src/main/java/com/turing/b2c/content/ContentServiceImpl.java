package com.turing.b2c.content;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.turing.b2c.mapper.ContentMapper;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Content;
import com.turing.b2c.model.pojo.ContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ContentServiceImpl implements  ContentService {

    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Override
    public Content findById(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Content> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 添加
     * @param entity
     */
    @Override
    public void save(Content entity) {
        contentMapper.insert(entity);
        redisTemplate.opsForHash().delete("contentList", entity.getCategoryId());
    }

    /**
     * 修改
     * @param entity
     */
    @Override
    public void update(Content entity) {
        // 清除原分类列表
        Content content = contentMapper.selectByPrimaryKey(entity.getId());
        redisTemplate.opsForHash().delete("contentList", content.getCategoryId());

        contentMapper.updateByPrimaryKey(entity);
        // 清除现分类列表
        if (!content.getCategoryId().equals(entity.getCategoryId())) {
            redisTemplate.opsForHash().delete("contentList", entity.getCategoryId());
        }
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        redisTemplate.opsForHash().delete("contentList", content.getCategoryId());
        contentMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除多条
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            Content content = contentMapper.selectByPrimaryKey(id);
            redisTemplate.opsForHash().delete("contentList", content.getCategoryId());
        }
        ContentExample example=new ContentExample();
        //把数组转换为list
        example.createCriteria().andIdIn(Arrays.asList(ids));
        contentMapper.deleteByExample(example);
    }

    @Override
    public SearchResult<Content> findPage(SearchParam searchParam) {
        //使用我们的分页插件
        PageHelper.startPage(searchParam.getPageNum(),searchParam.getPageSize());
        Page<Content> page=(Page<Content>) contentMapper.selectByExample(null);
        return new SearchResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据categoryId查询
     * @param categoryId
     * @return
     */
    //大key:把方法的返回值作为大key  一般把参数作为小key
    @Cacheable(cacheNames = "contentList")//定义缓存的名字 相当于给缓存定义一个key 值就是第一次查询的的集合的序列化的值
    @Override
    public List<Content> findByCategoryId(Long categoryId) {
        ContentExample example=new ContentExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo("1");
        example.setOrderByClause("sort_order");
        return contentMapper.selectByExample(example);
    }
}
