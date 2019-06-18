package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeiXinPayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.IdWorker;

import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    private WeiXinPayService weixinPayService;

   //生成二维码
    @RequestMapping("/createNative")
    public Map createNative(){
        IdWorker idworker=new IdWorker();
        return weixinPayService.creatPay(idworker.nextId()+"","1");
    }
}
