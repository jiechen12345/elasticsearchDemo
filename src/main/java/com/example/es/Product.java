package com.example.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by JieChen on 2023/6/8.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "product", shards = 3, replicas = 1)
public class Product {
    @Id
    private Long id;
    //    @Field(name = "title", type = FieldType.Text, analyzer = "ik_max_word")
    @Field(name = "title", type = FieldType.Text) //要分詞
    private String title;
    @Field(type = FieldType.Double)
    private Double price;
    @Field(type = FieldType.Keyword) //不分詞
    private String category;
    @Field(type = FieldType.Keyword, index = false) //不分詞
    private String images;
}
