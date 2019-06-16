package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

public interface BrandService {
    //访问service的接口,然后自动调用实现类的方法
    //查询品牌所有
    public List<TbBrand> findAll();
    //分页查询品牌
    public PageResult findPage(int pageNum, int pageSize);
    //增加
    public void add(TbBrand tbBrand);

    //品牌修改  两个方法
    public TbBrand findOne(Long id);
    public void update(TbBrand tbBrand);

    //品牌删除
    public void delete(Long[] ids);


    //条件查询
    public PageResult findEvent(TbBrand tbBrand, int pageNum, int pageSize);

    //品牌下拉框数据
    List<Map> selectOptionList();
}
