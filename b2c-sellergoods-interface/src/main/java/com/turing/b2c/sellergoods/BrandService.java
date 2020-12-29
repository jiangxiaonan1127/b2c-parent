package com.turing.b2c.sellergoods;

import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Brand;

import java.util.List;

public interface BrandService {
    Brand findById(Long id);


    List<Brand> findAll();


    void save(Brand entity);


    void update(Brand entity);


    void delete(Long id);


    void delete(Long[] ids);


    SearchResult<Brand> findPage(SearchParam searchParam);
}
