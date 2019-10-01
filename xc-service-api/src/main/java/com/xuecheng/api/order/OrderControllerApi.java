package com.xuecheng.api.order;

import com.xuecheng.framework.domain.order.request.CreateOrderRequest;
import com.xuecheng.framework.domain.order.response.OrderResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * Created by admin on 2018/2/7.
 */

@RequestMapping("/order")
@Api(value = "订单管理",description = "订单管理",tags = {"订单管理"})
public interface OrderControllerApi {

    /***********************************订单管理*********************************/
    @PostMapping("/create")
    @ApiOperation("创建订单")
    public OrderResult createOrder(@RequestBody CreateOrderRequest createOrderRequest);

    @GetMapping("/get/{orderId}")
    @ApiOperation("获取订单")
    public OrderResult getOrder(@PathVariable("orderId") String orderId);


}
