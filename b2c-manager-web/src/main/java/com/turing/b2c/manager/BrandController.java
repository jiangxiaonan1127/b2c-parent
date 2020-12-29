package com.turing.b2c.manager;

import com.alibaba.dubbo.config.annotation.Reference;
import com.turing.b2c.model.dto.MsgBox;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Brand;
import com.turing.b2c.sellergoods.BrandService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
public class BrandController {

    @Reference
    private BrandService brandService;

    @GetMapping("/brand")
    public List<Brand> findAll(){
        return brandService.findAll();
    }
    //分页查询所有数据
    @GetMapping("/brands")
    public SearchResult<Brand> findAll(SearchParam searchParam){
        return brandService.findPage(searchParam);
    }

    //查询单个
    @GetMapping("/brand/{id}")
    public Brand findById(@PathVariable("id") Long id){
        return brandService.findById(id);
    }

    //增加
    @PostMapping("/brand")
    public MsgBox save(@RequestBody Brand entity){
        try {
            brandService.save(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

    @PutMapping("/brand")
    public MsgBox update(@RequestBody Brand entity){
        try {
            brandService.update(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }


    @DeleteMapping("/brand/{ids}")
    public MsgBox delete(@PathVariable("ids") Long[] ids){
        try {
            brandService.delete(ids);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

}
