package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;
    //查询品牌所有
    @RequestMapping("/find")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }
    //查询分页品牌
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        PageResult pageR = brandService.findPage(page, rows);
        return pageR;
    }
    //添加品牌
    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand tbBrand){
        try{
            brandService.add(tbBrand);
            return new Result(true,"保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"保存失败");
        }
    }
    //修改品牌  两个方法
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }
    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand tbBrand){
        try{
            brandService.update(tbBrand);
            return new Result(true,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }
    //品牌删除
    @RequestMapping("/del")
    public Result del(Long[] ids){
        try {
            brandService.delete(ids);
            return new Result(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除错误");
        }
    }

    //品牌条件查询
    @RequestMapping("/findEvent")
    public PageResult findEvent(@RequestBody TbBrand tbBrand, int page, int rows){
        PageResult pageR = brandService.findEvent(tbBrand,page,rows);
        return pageR;
    }

    //
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {
        return brandService.selectOptionList();
    }
}
