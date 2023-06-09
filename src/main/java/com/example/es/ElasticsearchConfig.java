package com.example.es;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.stereotype.Component;

import javax.sound.sampled.Port;

/**
 * Created by JieChen on 2023/6/8.
 */
@Component
@ConfigurationProperties(prefix="elasticsearch")
@Configuration
@Data
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
    private String host ;
    private Integer port ;
    @Override
    public RestHighLevelClient elasticsearchClient() {
        RestClientBuilder restClientBuilder= RestClient.builder(new HttpHost(host, port));
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        return  restHighLevelClient;
    }
}
