package com.pinyougou.pay.service;

import java.util.Map;

public interface WeiXinPayService {
    //生成二维码
    public Map creatPay(String out_trade_no,String  total_fee);
    //查询支付状态
    public Map queryPayStatus(String out_trade_no);
}
