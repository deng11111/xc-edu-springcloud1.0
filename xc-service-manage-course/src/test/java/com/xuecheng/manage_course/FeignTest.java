package com.xuecheng.manage_course;

import com.xuecheng.framework.domain.portalview.ViewCourse;
import com.xuecheng.manage_course.client.PortalViewClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by mrt on 2018/4/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FeignTest {

    @Autowired
    PortalViewClient portalViewClient;

    //通过地址调用
    @Test
    public void testFeign1() {
        ViewCourse viewCourse = portalViewClient.findById("123");
        System.out.println(viewCourse);
    }

}
