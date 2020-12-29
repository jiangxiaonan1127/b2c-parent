package com.turing.b2c.repository;

import com.turing.b2c.model.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 类似ES的一个mapper映射
 */
public interface ItemRepository extends ElasticsearchRepository<Item,Long> {

}
