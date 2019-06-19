package com.pinyougou.order.service;

import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbPayLog;
import entity.PageResult;

import java.util.List;
public interface OrderService {


	 //返回全部列表

	public List<TbOrder> findAll();
	
	

	 //返回分页列表

	public PageResult findPage(int pageNum, int pageSize);
	
	

	 //增加

	public void add(TbOrder order);
	
	

	 //修改
	public void update(TbOrder order);
	

	//根据ID获取实体

	public TbOrder findOne(Long id);
	
	

	 // 批量删除

	public void delete(Long[] ids);

	//分页 pageNum 当前页 码 pageSize 每页记录数
	public PageResult findPage(TbOrder order, int pageNum, int pageSize);


	 //根据用户查询payLog

	public TbPayLog searchPayLogFromRedis(String userId);


	 //修改订单状态
	//out_trade_no 支付订单号
	 //transaction_id 微信返回的交易流水号

	public void updateOrderStatus(String out_trade_no,String transaction_id);



}
