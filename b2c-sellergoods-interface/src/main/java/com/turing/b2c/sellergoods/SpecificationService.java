package com.turing.b2c.sellergoods;

import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Specification;
import com.turing.b2c.model.pojo.union.SpecUnion;

import java.util.List;

public interface SpecificationService {
    Specification findById(Long id);


    List<Specification> findAll();


    void save(Specification entity);


    void update(Specification entity);


    void delete(Long id);


    void delete(Long[] ids);


    SearchResult<Specification> findPage(SearchParam searchParam);


    SpecUnion findUnionById(Long id);

    void saveUnion(SpecUnion specUnion);

    void updateUnion(SpecUnion specUnion);
}
