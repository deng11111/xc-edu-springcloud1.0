package com.xuecheng.govern.gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import javax.ws.rs.core.Response.Status;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName MyAuthenticationFilter
 * @Author 邓元粮
 * @Date 2019/9/30 15:55
 * @Version 1.0
 **/
public class MyAuthenticationFilter implements HttpServerFilter {
    
    /*
     *@Description 代表过滤器级别 数字越小级别越高
     * @Author 邓元粮
     * @MethodName getOrder
     * @Datetime 15:56 2019/9/30
     * @Param []
     * @return int
     **/
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx requestEx) {
        String microserviceName = invocation.getMicroserviceName();
        Set<String> noAuthSet = genNoAuthSet();
        if (!noAuthSet.contains(microserviceName)) {//无需过滤
            String athentication = requestEx.getHeader("Athentication");
            if (StringUtils.isBlank(athentication)) {
                return Response.failResp(new InvocationException(Status.UNAUTHORIZED, "authentication failed"));
            }
        }else{//无需过滤
        }
        return null;
    }

    private Set<String> genNoAuthSet(){
        Set<String> noAuthSet = new HashSet<>();
        noAuthSet.add("xc-service-search");
//        noAuthSet.add("xc-service-portalview");
        noAuthSet.add("xc-service-learning");
        return noAuthSet;
    }
}
