package com.turing.b2c.sellergoods;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.turing.b2c.mapper.SpecificationMapper;
import com.turing.b2c.mapper.SpecificationOptionMapper;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Specification;
import com.turing.b2c.model.pojo.SpecificationExample;
import com.turing.b2c.model.pojo.SpecificationOption;
import com.turing.b2c.model.pojo.SpecificationOptionExample;
import com.turing.b2c.model.pojo.union.SpecUnion;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Service
public class SpecificationServiceImpl implements  SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SpecificationOptionMapper  specificationOptionMapper;
    @Override
    public Specification findById(Long id) {
        return specificationMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Specification> findAll() {
        return specificationMapper.selectByExample(null);
    }

    @Override
    public void save(Specification entity) {
        specificationMapper.insert(entity);
    }

    @Override
    public void update(Specification entity) {
        specificationMapper.updateByPrimaryKey(entity);
    }

    @Override
    public void delete(Long id) {
        specificationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        //xian 删zi
        SpecificationOptionExample ex=new SpecificationOptionExample();
        ex.createCriteria().andSpecIdIn(Arrays.asList(ids));
        specificationOptionMapper.deleteByExample(ex);

        SpecificationExample example=new SpecificationExample();
        //把数组转换为list
        example.createCriteria().andIdIn(Arrays.asList(ids));
        specificationMapper.deleteByExample(example);
    }

    @Override
    public SearchResult<Specification> findPage(SearchParam searchParam) {
        //使用我们的分页插件
        PageHelper.startPage(searchParam.getPageNum(),searchParam.getPageSize());
        Page<Specification> page=(Page<Specification>) specificationMapper.selectByExample(null);
        return new SearchResult(page.getTotal(),page.getResult());
    }

    @Override
    public SpecUnion findUnionById(Long id) {
        //查询规格对象
        Specification spec = specificationMapper.selectByPrimaryKey(id);
        SpecificationOptionExample example=new SpecificationOptionExample();
        example.createCriteria().andSpecIdEqualTo(id);
        List<SpecificationOption> specOptionsList = specificationOptionMapper.selectByExample(example);
        return new SpecUnion(spec,specOptionsList);
    }

    @Override
    public void saveUnion(SpecUnion specUnion) {
        //先添加规格表
        Specification spec = specUnion.getSpec();
        specificationMapper.insertSelective(spec);
        //详情
        List<SpecificationOption> specOptionList = specUnion.getSpecOptionList();
        for(SpecificationOption specOption :specOptionList){
            specOption.setSpecId(spec.getId());
            specificationOptionMapper.insertSelective(specOption);
        }
    }

    @Override
    public void updateUnion(SpecUnion specUnion) {
        //先修改规格表
        Specification spec= specUnion.getSpec();
        specificationMapper.updateByPrimaryKey(spec);
        //删除规格下所有规格详情
        SpecificationOptionExample ex=new SpecificationOptionExample();
        ex.createCriteria().andSpecIdEqualTo(spec.getId());
        specificationOptionMapper.deleteByExample(ex);
        //添加规格详情
        List<SpecificationOption> specOptionList = specUnion.getSpecOptionList();
        for(SpecificationOption specOption :specOptionList){
            specOption.setSpecId(spec.getId());
            specificationOptionMapper.insertSelective(specOption);
        }
    }
}
