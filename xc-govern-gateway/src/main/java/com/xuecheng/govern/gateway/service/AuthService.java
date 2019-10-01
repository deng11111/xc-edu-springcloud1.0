package com.xuecheng.govern.gateway.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.ucenter.ext.UserTokenStore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by mrt on 2018/5/21.
 */
@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //查询token的有效期，返回秒
    public Long getExpireByToken(String token){
        if(StringUtils.isEmpty(token)){
             return -2l;
        }
        //查询key的有效期，令牌的完整key是access:token
//        Long expire = stringRedisTemplate.getExpire("access:" + token, TimeUnit.SECONDS);
        Long expire = stringRedisTemplate.getExpire("user_token:" + token, TimeUnit.SECONDS);
        return expire;
    }
    //查询token
    public UserTokenStore getUserToken(String token){
        String userToken = "user_token:"+token;
        String userTokenString = stringRedisTemplate.opsForValue().get(userToken);
        if(userToken!=null){
            UserTokenStore userTokenStore = null;
            try {
                userTokenStore = JSON.parseObject(userTokenString, UserTokenStore.class);
            } catch (Exception e) {
                LOGGER.error("getUserToken from redis and execute JSON.parseObject error {}",e.getMessage());
                e.printStackTrace();
            }
            return userTokenStore;
        }
        return null;
    }
}
