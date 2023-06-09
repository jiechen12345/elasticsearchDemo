package com.example.es;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpringdataEsApplicationTests {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Test
    public void createIndex() {
        //它會自動比對你的 Entity
        System.out.println("create index...........");
    }

    @Test
    public void deleteIndex() {
        boolean falg = elasticsearchRestTemplate.deleteIndex(Product.class);
        System.out.println("delete index..........." + falg);
    }


    @Test
    void contextLoads() {
    }

}
