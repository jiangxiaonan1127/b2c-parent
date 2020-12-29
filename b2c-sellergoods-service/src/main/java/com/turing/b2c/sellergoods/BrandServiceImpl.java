package com.turing.b2c.sellergoods;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.turing.b2c.mapper.BrandMapper;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Brand;
import com.turing.b2c.model.pojo.BrandExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Service
public class BrandServiceImpl implements  BrandService {

    @Autowired
    private BrandMapper brandMapper;
    @Override
    public Brand findById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectByExample(null);
    }

    @Override
    public void save(Brand entity) {
        brandMapper.insert(entity);
    }

    @Override
    public void update(Brand entity) {
        brandMapper.updateByPrimaryKey(entity);
    }

    @Override
    public void delete(Long id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        BrandExample example=new BrandExample();
        //把数组转换为list
        example.createCriteria().andIdIn(Arrays.asList(ids));
        brandMapper.deleteByExample(example);
    }

    @Override
    public SearchResult<Brand> findPage(SearchParam searchParam) {
        //使用我们的分页插件
        PageHelper.startPage(searchParam.getPageNum(),searchParam.getPageSize());
        Page<Brand> page=(Page<Brand>) brandMapper.selectByExample(null);
        return new SearchResult(page.getTotal(),page.getResult());
    }
}
