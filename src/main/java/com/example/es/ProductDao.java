package com.example.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by JieChen on 2023/6/8.
 */
@Repository
public interface ProductDao extends ElasticsearchRepository<Product,Long> {
}
