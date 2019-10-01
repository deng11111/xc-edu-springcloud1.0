package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import com.xuecheng.govern.gateway.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component
public class LoginFilter extends ZuulFilter {

    private static final Logger LOG = LoggerFactory.getLogger(LoginFilter.class);

    private static final String OPENAPI = "/openapi";

    @Autowired
    AuthService authService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;//int值来定义过滤器的执行顺序，数值越小优先级越高
    }

    @Override
    public boolean shouldFilter() {// 该过滤器需要执行
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String requestURI = request.getRequestURI();
        return !requestURI.startsWith(OPENAPI);
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        HttpServletRequest request = requestContext.getRequest();
        //取出cookie中的token，cookie中没有令牌拒绝访问
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String access_token = cookieMap.get("uid");
        if (StringUtils.isEmpty(access_token)){
            access_denied();
            return null;
        }

        //从redis查询 token，返回过期时间，如果令牌不存在则拒绝访问
        Long expireByToken = authService.getExpireByToken(access_token);
        if (expireByToken<0){
            access_denied();
            return null;
        }
        //取出头部信息Authorization，没有jwt令牌拒绝访问
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization) || authorization.indexOf("Bearer")<0){
            access_denied();
            return null;
        }
        return null;
/*        //从redis查询usertoken
        UserTokenStore userTokenStore = authService.getUserToken(access_token);
        if(userTokenStore == null){
            access_denied();
            return null;
        }
        //构建向资源服务传递的用户身份对象
        UserToken userToken = new UserToken();
        BeanUtils.copyProperties(userTokenStore,userToken);
        //将userToken转为json串
        String userTokenJson = JSON.toJSONString(userToken);
        //向header中添加userToken
        requestContext.addZuulRequestHeader("UserToken",userTokenJson);
        return null;*/
    }

    //身份不合法截断请求
    private void access_denied(){
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        requestContext.setSendZuulResponse(false);// 拒绝访问
        requestContext.setResponseStatusCode(200);// 设置响应状态码
        ResponseResult unauthenticated = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String jsonString = JSON.toJSONString(unauthenticated);
        requestContext.setResponseBody(jsonString);
        requestContext.getResponse().setContentType("application/json;charset=UTF-8");
    }
}
