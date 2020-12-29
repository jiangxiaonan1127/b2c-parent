package com.turing.b2c.sellergoods;

import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.ItemCat;

import java.util.List;

public interface ItemCatService {
    ItemCat findById(Long id);


    List<ItemCat> findAll();


    void save(ItemCat entity);


    void update(ItemCat entity);


    void delete(Long id);


    void delete(Long[] ids);


    SearchResult<ItemCat> findPage(SearchParam searchParam);

    List<ItemCat> findByParentId(Long parentId);
}
