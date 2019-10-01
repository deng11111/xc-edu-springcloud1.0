package com.xuecheng.api.test;

import com.xuecheng.framework.domain.test.UserTest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by mrt on 2018/3/29.
 */
@RestController
public class TestController implements  TestControllerApi {

    @Override
    public UserTest get(@PathVariable String id) throws IOException {
        return null;
    }
}
