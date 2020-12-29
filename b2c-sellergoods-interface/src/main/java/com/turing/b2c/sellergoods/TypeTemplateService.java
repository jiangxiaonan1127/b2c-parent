package com.turing.b2c.sellergoods;

import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.TypeTemplate;
import com.turing.b2c.model.pojo.union.SpecUnion;

import java.util.List;

public interface TypeTemplateService {
    TypeTemplate findById(Long id);


    List<TypeTemplate> findAll();


    void save(TypeTemplate entity);


    void update(TypeTemplate entity);


    void delete(Long id);


    void delete(Long[] ids);


    SearchResult<TypeTemplate> findPage(SearchParam searchParam);

    List<SpecUnion> findSpecUnionsById(Long id);
}
