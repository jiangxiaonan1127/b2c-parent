package com.turing.b2c.search;

import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Item;

import java.util.List;

public interface ItemService {
    Item findById(Long id);

    List<Item> findAll();

    void save(Item entity);

    void update(Item entity);

    void delete(Long id);


    void delete(Long[] ids);

    SearchResult<Item> findPage(SearchParam searchParam);
    //ES的新增方法
    void index();
}
