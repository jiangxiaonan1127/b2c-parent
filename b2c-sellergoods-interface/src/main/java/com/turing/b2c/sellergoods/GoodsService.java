package com.turing.b2c.sellergoods;

import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Goods;

import java.util.List;

public interface GoodsService {
    Goods findById(Long id);


    List<Goods> findAll();


    void save(Goods entity);


    void update(Goods entity);


    void delete(Long id);


    void delete(Long[] ids);


    SearchResult<Goods> findPage(SearchParam searchParam);

    void updateAuditStatus(Long [] ids,String auditStatus);
}
