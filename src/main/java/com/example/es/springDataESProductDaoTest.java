package com.example.es;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by JieChen on 2023/6/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class springDataESProductDaoTest {
    @Autowired
    private ProductDao productDao;

    //新增
    @Test
    public void createProduct() {
        Product product = new Product(2L, "iphone15", 24000.0, "手機", "iphone15.jpg");
        productDao.save(product);
    }

    //修改
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
        System.out.print(product);
    }

    //查詢
    @Test
    public void findALLProduct() {
        Iterable<Product> products = productDao.findAll();
        products.forEach(product -> System.out.print(product));
    }

}
