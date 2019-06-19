package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeiXinPayService;
import com.pinyougou.pojo.TbPayLog;
import entity.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {
    @Reference
    private WeiXinPayService weixinPayService;

    @Reference
    private OrderService orderService;


    //生成二维码
    @RequestMapping("/createNative")
    public Map createNative() {
       /* IdWorker idworker = new IdWorker();
        return weixinPayService.creatPay(idworker.nextId() + "", "1");*/

        //获取当前用户
        String userId= SecurityContextHolder.getContext().getAuthentication().getName();
        //到redis查询支付日志
        TbPayLog payLog = orderService.searchPayLogFromRedis(userId);
        //判断支付日志存在
        if(payLog!=null){
            return weixinPayService.creatPay(payLog.getOutTradeNo(),payLog.getTotalFee()+"");
        }else{
            return new HashMap();
        }

    }

    //支付状态
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        Result result = null;
        int i=0;
        while (true) {
            //调用查询接口

            Map<String, String> map = weixinPayService.queryPayStatus(out_trade_no);

            System.out.println(map.get("trade_state"));
            if (map == null) {//出错
                result = new Result(false, "支付出错");
                break;
            }
            if (map.get("trade_state").equals("SUCCESS")) {//如果成功
                result = new Result(true, "支付成功");

                //  支付成功 后修改订单状态
                orderService.updateOrderStatus(out_trade_no, map.get("transaction_id"));

                break;
            }
            if (StringUtils.equals("USERPAYING", map.get("trade_state"))) {//支付中
                System.out.println("支付中");
                try {
                    request.getRequestDispatcher("paying.html").forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*result = new Result(true, "支付中");
                break;*/
            }
            try {
                Thread.sleep(3000);//间隔三秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i++;
            if(i>=100){
                result=new  Result(false, "二维码超时");
                break;
            }

        }
        return result;
    }

}
