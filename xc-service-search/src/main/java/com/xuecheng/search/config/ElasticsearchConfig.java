package com.xuecheng.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class ElasticsearchConfig {

    @Value("${xuecheng.elasticsearch.hostlist}")
    private String hostlist;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        List<HttpHost> httpHosts = new ArrayList<HttpHost>();
        String[] split = hostlist.split(",");
        for(int i=0;i<split.length;i++){
            String item = split[i];
            httpHosts.add(new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http"));
        }
        //elasticsearchSettings.getHttpHost().stream().forEach(item -> httpHosts.add(new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http")));
        return new RestHighLevelClient(
                RestClient.builder(httpHosts.stream().toArray(HttpHost[]::new)));
    }

    @Bean
    public RestClient restClient(){
        List<HttpHost> httpHosts = new ArrayList<HttpHost>();
        String[] split = hostlist.split(",");
        for(int i=0;i<split.length;i++){
            String item = split[i];
            httpHosts.add(new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http"));
        }
        return RestClient.builder(httpHosts.stream().toArray(HttpHost[]::new)).build();
    }

}
