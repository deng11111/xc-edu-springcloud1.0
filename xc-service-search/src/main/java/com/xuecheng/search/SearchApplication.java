package com.xuecheng.search;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@EnableDiscoveryClient
//@EnableFeignClients
@EnableServiceComb
@SpringBootApplication//扫描所在包及子包的bean，注入到ioc中
@ComponentScan(basePackages={"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描framework中通用类
@ComponentScan(basePackages={"com.xuecheng.search"})//扫描本项目下的所有类
public class SearchApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SearchApplication.class, args);
    }


}