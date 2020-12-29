package com.turing.b2c.sellergoods;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.turing.b2c.mapper.ItemCatMapper;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.ItemCat;
import com.turing.b2c.model.pojo.ItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ItemCatServiceImpl implements  ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper ;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public ItemCat findById(Long id) {
        return itemCatMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return itemCatMapper.selectByExample(null);
    }

    @Override
    public void save(ItemCat entity) {
        itemCatMapper.insert(entity);
    }

    @Override
    public void update(ItemCat entity) {
        itemCatMapper.updateByPrimaryKey(entity);
    }

    @Override
    public void delete(Long id) {
        itemCatMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        ItemCatExample example=new ItemCatExample();
        //把数组转换为list
        example.createCriteria().andIdIn(Arrays.asList(ids));
        itemCatMapper.deleteByExample(example);
    }

    @Override
    public SearchResult<ItemCat> findPage(SearchParam searchParam) {
        //使用我们的分页插件
        PageHelper.startPage(searchParam.getPageNum(),searchParam.getPageSize());
        Page<ItemCat> page=(Page<ItemCat>) itemCatMapper.selectByExample(null);
        return new SearchResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        ItemCatExample example=new ItemCatExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        // CRUD商品分类的时候，都会路由该方法，因此定义在这里比较合适
        // 缓存结构：key-商品分类名称：value-模板id
        cacheTypeTemplateId();
        return itemCatMapper.selectByExample(example);
    }
    private void cacheTypeTemplateId(){
        List<ItemCat> list = findAll();
        list.forEach(itemCat -> {
            //写入redis itemCat 大Key, name 小Key  typeId value
            redisTemplate.opsForHash().put("itemCat",itemCat.getName(),itemCat.getTypeId());
        });
    }
}
