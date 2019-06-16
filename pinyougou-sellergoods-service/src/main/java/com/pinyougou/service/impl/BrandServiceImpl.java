package com.pinyougou.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {


    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    //查询品牌所有
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    //分页查询品牌所有
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //添加品牌
    @Override
    public void add(TbBrand tbBrand) {
        brandMapper.insert(tbBrand);
    }
    //修改品牌 两个方法
    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(TbBrand tbBrand) {
        brandMapper.updateByPrimaryKey(tbBrand);
    }
    //品牌删除
    @Override
    public void delete(Long[] ids) {
        for (Long id:ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }
    //条件查询
    @Override
    public PageResult findEvent(TbBrand tbBrand, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TbBrandExample tbBrandExample=new TbBrandExample();
        TbBrandExample.Criteria criteria = tbBrandExample.createCriteria();
        if(tbBrand!=null){
            if(tbBrand.getName()!=null&&tbBrand.getName().length()>0){
                criteria.andNameLike(tbBrand.getName());
            }
            if(tbBrand.getFirstChar()!=null&&tbBrand.getFirstChar().length()>0){
                criteria.andFirstCharLike(tbBrand.getFirstChar());
            }
        }
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(tbBrandExample);
        return new PageResult(page.getTotal(), page.getResult());
    }
    @Override
    public List<Map> selectOptionList() {
        return brandMapper.selectOptionList();
    }
}
