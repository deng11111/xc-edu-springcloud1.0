package com.xuecheng.manage_course;

import com.xuecheng.framework.domain.portalview.ViewCourse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Created by mrt on 2018/4/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RibbonTest {

    @Autowired
    RestTemplate restTemplate;

    //通过地址调用
    @Test
    public void testRibbon1() throws MalformedURLException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        String url = "http://127.0.0.1:40200/portalview/course/get/123";
        ResponseEntity<ViewCourse> forEntity = restTemplate.getForEntity(url, ViewCourse.class);
        ViewCourse viewCourse = forEntity.getBody();
        System.out.println(viewCourse);
    }

    //负载均衡调用
    @Test
    public void testRibbon2() {
        //服务id
        String serviceId = "XC-SERVICE-PORTALVIEW";
        for(int i=0;i<10;i++){
            //通过服务id调用
            ResponseEntity<ViewCourse> forEntity = restTemplate.getForEntity("http://" + serviceId + "/portalview/course/get/123", ViewCourse.class);
            ViewCourse viewCourse = forEntity.getBody();
            System.out.println(viewCourse);
        }
    }


}
