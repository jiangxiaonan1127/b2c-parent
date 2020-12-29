package com.turing.b2c.seller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.turing.b2c.model.dto.MsgBox;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.TypeTemplate;
import com.turing.b2c.model.pojo.union.SpecUnion;
import com.turing.b2c.sellergoods.TypeTemplateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;
    /*
    @GetMapping("/brands")
    public List<TypeTemplate> findAll(){
        return typeTemplateService.findAll();
    }*/
    //分页查询所有数据
    @GetMapping("/typeTemplates")
    public SearchResult<TypeTemplate> findAll(SearchParam searchParam){
        return typeTemplateService.findPage(searchParam);
    }

    //查询单个
    @GetMapping("/typeTemplate/{id}")
    public TypeTemplate findById(@PathVariable("id") Long id){
        return typeTemplateService.findById(id);
    }

    @GetMapping("/typeTemplate/specUnions/{id}")
    public List<SpecUnion> findSpecUnionsById(@PathVariable("id") Long id) {
        return typeTemplateService.findSpecUnionsById(id);
    }
    //增加
    @PostMapping("/typeTemplate")
    public MsgBox save(@RequestBody TypeTemplate entity){
        try {
            typeTemplateService.save(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

    @PutMapping("/typeTemplate")
    public MsgBox update(@RequestBody TypeTemplate entity){
        try {
            typeTemplateService.update(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }


    @DeleteMapping("/typeTemplate/{ids}")
    public MsgBox delete(@PathVariable("ids") Long[] ids){
        try {
            typeTemplateService.delete(ids);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

}
