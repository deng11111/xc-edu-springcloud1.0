package com.xuecheng.api.order;

import com.xuecheng.framework.domain.order.response.PayOrderResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mrt on 2018/5/26.
 */
@RequestMapping("/order/pay")
@Api(value = "支付管理",description = "支付管理",tags = {"支付管理"})
public interface PayControllerApi {

    @PostMapping("/createWeixinQrcode")
    @ApiOperation("创建支付二维码")
    public PayOrderResult createWeixinQrcode(@RequestParam("orderNumber") String orderNumber);

    @GetMapping("/queryWeixinPayStatus")
    @ApiOperation("查询订单支付结果")
    public PayOrderResult queryWeixinPayStatus(@RequestParam("orderNumber") String orderNumber);

}