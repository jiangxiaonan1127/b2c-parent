package com.turing.b2c.sellergoods;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.turing.b2c.mapper.BrandMapper;
import com.turing.b2c.mapper.GoodsMapper;
import com.turing.b2c.mapper.ItemMapper;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Goods;
import com.turing.b2c.model.pojo.GoodsExample;
import com.turing.b2c.model.pojo.Item;
import com.turing.b2c.model.pojo.ItemExample;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Service
public class GoodsServiceImpl implements  GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public Goods findById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Goods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    @Override
    public void save(Goods entity) {
        goodsMapper.insert(entity);
    }

    @Override
    public void update(Goods entity) {
        goodsMapper.updateByPrimaryKey(entity);
    }

    @Override
    public void delete(Long id) {
        goodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        GoodsExample example=new GoodsExample();
        //把数组转换为list
        example.createCriteria().andIdIn(Arrays.asList(ids));
        goodsMapper.deleteByExample(example);
    }

    @Override
    public SearchResult<Goods> findPage(SearchParam searchParam) {
        //使用我们的分页插件
        PageHelper.startPage(searchParam.getPageNum(),searchParam.getPageSize());
        Page<Goods> page=(Page<Goods>) goodsMapper.selectByExample(null);
        return new SearchResult(page.getTotal(),page.getResult());
    }

    @Override
    public void updateAuditStatus(Long[] ids, String auditStatus) {
        if ("1".equals(auditStatus)){
            //发送更新消息
            //首先根据商品id查询SKU列表
            ItemExample example = new ItemExample();
            example.createCriteria().andStatusEqualTo(auditStatus)
                    .andGoodsIdIn(Arrays.asList(ids));
            List<Item> items = itemMapper.selectByExample(example);
            //以JSON格式向RabbitMQ发送消息
            String json = JSON.toJSONString(items);
            rabbitTemplate.convertAndSend("b2c.direct","item.update",json);
        }
    }
}
