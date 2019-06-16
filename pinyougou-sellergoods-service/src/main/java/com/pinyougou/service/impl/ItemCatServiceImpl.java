package com.pinyougou.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemCatExample;
import com.pinyougou.pojo.TbItemCatExample.Criteria;
import com.pinyougou.sellergoods.service.ItemCatService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {


	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbItemCat> findAll() {
		return itemCatMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbItemCat> page=   (Page<TbItemCat>) itemCatMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbItemCat itemCat) {

		itemCatMapper.insert(itemCat);
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbItemCat itemCat){
		itemCatMapper.updateByPrimaryKey(itemCat);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbItemCat findOne(Long id){
		return itemCatMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	/*@Override
	//递归删除
	public void delete(Long[] ids){
		for(Long id:ids){
			TbItemCatExample example=new TbItemCatExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(id);
			List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(example);//根据id查询子
			if(tbItemCats.isEmpty()) {
				itemCatMapper.deleteByPrimaryKey(id);
			}else{
				Long[] id2 = new Long[tbItemCats.size()];
				for (TbItemCat i:tbItemCats) {
					for(int ii=0;ii<id2.length;ii++){
						id2[ii]=i.getId();
					}
				}
				del(id2);
			}
		}
	}*/
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			TbItemCatExample example=new TbItemCatExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(id);
			List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(example);//根据id查询子
			if(tbItemCats.isEmpty()) {
				itemCatMapper.deleteByPrimaryKey(id);
			}else{
				throw new RuntimeException();
			}
		}
	}
	
	
		@Override
	public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbItemCatExample example=new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		
		if(itemCat!=null){			
						if(itemCat.getName()!=null && itemCat.getName().length()>0){
				criteria.andNameLike("%"+itemCat.getName()+"%");
			}
	
		}
		
		Page<TbItemCat> page= (Page<TbItemCat>)itemCatMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	@Autowired
	private RedisTemplate redisTemplate;
	//根据id查询
	@Override
	public List<TbItemCat> findItemCatById(Long id) {
		TbItemCatExample example=new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(example);
		//每次执行查询时 把品牌存入缓存
		List<TbItemCat> all = findAll();
		for(TbItemCat itemCat:all){
			redisTemplate.boundHashOps("itemCat").put(itemCat.getName(),itemCat.getTypeId());
		}
		System.out.println("更新缓存:商品分类");

		return tbItemCats;
	}
	
}
