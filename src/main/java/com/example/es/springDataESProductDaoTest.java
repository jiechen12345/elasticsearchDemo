package com.example.es;


import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JieChen on 2023/6/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class springDataESProductDaoTest {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    //新增  POSTMAN, GET http://localhost:9200/product/_doc/2
    @Test
    public void createProduct() {
        Product product = new Product(2L, "iphone 15", 24000.0, "手機", "iphone15.jpg");
        productDao.save(product);
    }

    //修改 POSTMAN, GET http://localhost:9200/product/_doc/2
    @Test
    public void updateProduct() {
        Product product = new Product();
        product.setId(2L);
        product.setPrice(25000.0);
        productDao.save(product);
    }

    //查詢
    @Test
    public void findProduct() {
        Product product = productDao.findById(2L).get();
        System.out.println(product);
    }

    //查詢
    @Test
    public void findALLProduct() {
        Iterable<Product> products = productDao.findAll();
        products.forEach(product -> System.out.println(product));
    }

    //删除 POSTMAN, GET http://localhost:9200/product/_doc/2
    @Test
    public void delete() {
        Product product = new Product();
        product.setId(2L);
        productDao.delete(product);
    }


    //批次新增
    @Test
    public void saveAll() {
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Product product = new Product();
            product.setId(Long.valueOf(i));
            product.setTitle("iphone " + i);
            product.setCategory("手機");
            product.setPrice(20000.0 + i * 1000);
            product.setImages("IP" + i + ".jpg");
            productList.add(product);
        }
        productDao.saveAll(productList);
    }

    //term條件查詢
    @Test
    public void termQuery() {
        //類似SQL的fieldname=value
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", "iphone");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", "手機");
        Iterable<Product> products = productDao.search(termQueryBuilder);
        System.out.println("*************************************");
        products.forEach(product -> System.out.println(product));
    }

    //全部查詢+分頁
    @Test
    public void findByPageable() {
        //排序方式
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        int currentPage = 0;//當前頁
        int pageSize = 5;//每頁幾筆
        //宣告分頁物件
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);
        //進行查詢
        Page<Product> productPage = productDao.findAll(pageRequest);
        productPage.getContent().forEach(product -> System.out.println(product));
    }

    /**
     * 條件查詢加分頁
     */
    @Test
    public void termQueryByPage() {
        int currentPage = 0;
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", "手機");

        Iterable<Product> products = productDao.search(termQueryBuilder, pageRequest);
        products.forEach(product -> System.out.println(product));
    }

    //terms條件查詢
    @Test
    public void termsQuery() {
        //類似SQL：id in (1,2,3,4,5)
        TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("id", new Object[]{1, 2, 3, 4, 5});
        Iterable<Product> products = productDao.search(termsQueryBuilder);
        System.out.println("*************************************");
        products.forEach(product -> System.out.println(product));
    }


    //terms條件查詢加排序
    @Test
    public void termsAndSortQuery() {
        TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("id", new Object[]{1, 2, 3, 4, 5});
        Sort sort = Sort.by(Sort.Order.asc("id"));
        PageRequest pageRequest = PageRequest.of(0, 10000, sort);
        Iterable<Product> products = productDao.search(termsQueryBuilder, pageRequest);
        System.out.println("*************************************");
        products.forEach(System.out::println);
    }

    //    範圍查詢 price > 30000 and price < 32000
    @Test
    public void rangeQuery() {
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("price");
        rangeQueryBuilder.gt(28000);
        rangeQueryBuilder.lt(35000);

        rangeQueryBuilder = QueryBuilders.rangeQuery("price")
                .gte(28000)
                .lte(35000);
        Sort sort = Sort.by(Sort.Order.asc("price"));
        PageRequest pageRequest = PageRequest.of(0, 10000, sort);
        Iterable<Product> products = productDao.search(rangeQueryBuilder, pageRequest);
        products.forEach(product -> System.out.println(product));
    }

    //bool 複雜查詢
    @Test
    public void boolQuery() {
        //SQL： category=手機 and title=iphone or (price >= 30000 and price <= 40000)
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("category", "手機"))
                .must(QueryBuilders.termQuery("title", "iphone"))
                .should(QueryBuilders.rangeQuery("price").gte(30000).lte(40000));

        Sort sort = Sort.by(Sort.Order.asc("price"));
        PageRequest pageRequest = PageRequest.of(0, 10000, sort);
        Iterable<Product> products = productDao.search(boolQueryBuilder, pageRequest);
        products.forEach(product -> System.out.println(product));
    }

    //bool 複雜查詢
    @Test
    public void boolQuery2() {
        //SQL :AND (category = '手機' OR ( title = 'samsung')) AND (price >= 30000 AND price <= 40000)
        // 創建查詢條件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 創建 category = '手機' 的子查詢
        BoolQueryBuilder categoryQueryBuilder = QueryBuilders.boolQuery();
        categoryQueryBuilder.should(QueryBuilders.termQuery("category", "手機"));
        // 創建 title = 'samsung' 的子查詢
        BoolQueryBuilder titleQueryBuilder = QueryBuilders.boolQuery();
        titleQueryBuilder.should(QueryBuilders.termQuery("title", "samsung"));
        // 將 category 和 title 的子查詢組合為 OR 關係
        categoryQueryBuilder.should(titleQueryBuilder);
        // 將 category 和 title 的子查詢組合為 AND 關係
        boolQueryBuilder.must(categoryQueryBuilder);
        // 創建 price >= 30000 AND price <= 40000 的子查詢
        RangeQueryBuilder priceQueryBuilder = QueryBuilders.rangeQuery("price").gte(30000).lte(40000);
        // 將 price 的子查詢添加到布爾查詢中
        boolQueryBuilder.must(priceQueryBuilder);
        // 創建排序規則
        Sort sort = Sort.by(Sort.Order.asc("price"));
        // 創建分頁請求對象
        Pageable pageable = PageRequest.of(0, 10000, sort);

        // 執行查詢
        Iterable<Product> products = productDao.search(boolQueryBuilder, pageable);
        // 輸出結果
        System.out.println("*************************************");
        products.forEach(System.out::println);
    }
}
