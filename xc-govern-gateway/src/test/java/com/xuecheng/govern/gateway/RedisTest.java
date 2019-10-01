package com.xuecheng.govern.gateway;

import com.alibaba.fastjson.JSON;
import com.xuecheng.govern.gateway.service.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by mrt on 2018/5/21.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AuthService authService;

    @Test
    public void testGetExpireByToken(){
//        Long expire = stringRedisTemplate.getExpire("access:e045b8f0-819b-42ff-9aa6-37de8a1a374b", TimeUnit.SECONDS);
//        System.out.println(expire);
        Long expireByToken = authService.getExpireByToken("e045b8f0-819b-42ff-9aa6-37de8a1a374b");
        System.out.println(expireByToken);
        Long expire = stringRedisTemplate.getExpire("access_to_refresh:e045b8f0-819b-42ff-9aa6-37de8a1a374b");
        System.out.println(expire);
    }
    @Test
    public void testToken(){
        //定义key
        String key = "user_token:9734b68f-cf5e-456f-9bd6-df578c711390";
        //定义Map
        Map<String,String> mapValue = new HashMap<>();
        mapValue.put("id","mrt");
        mapValue.put("username","mrt");
        String value = JSON.toJSONString(mapValue);
        //向redis中存储字符串
        stringRedisTemplate.boundValueOps(key).set(value,60, TimeUnit.SECONDS);
        //读取过期时间，已过期返回-2
        Long expire = stringRedisTemplate.getExpire(key);
        //根据key获取value
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);
    }
}
