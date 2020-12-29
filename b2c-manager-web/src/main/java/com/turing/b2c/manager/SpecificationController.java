package com.turing.b2c.manager;

import com.alibaba.dubbo.config.annotation.Reference;
import com.turing.b2c.model.dto.MsgBox;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Specification;
import com.turing.b2c.model.pojo.union.SpecUnion;
import com.turing.b2c.sellergoods.SpecificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;

    @GetMapping("/specification")
    public List<Specification> findAll(){
        return specificationService.findAll();
    }
    //分页查询所有数据
    @GetMapping("/specifications")
    public SearchResult<Specification> findAll(SearchParam searchParam){
        return specificationService.findPage(searchParam);
    }

    //查询单个
    @GetMapping("/specification/{id}")
    public SpecUnion findById(@PathVariable("id") Long id){
        return specificationService.findUnionById(id);
    }

    //增加
    @PostMapping("/specification")
    public MsgBox save(@RequestBody SpecUnion entity){
        try {
            specificationService.saveUnion(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

    @PutMapping("/specification")
    public MsgBox update(@RequestBody SpecUnion entity){
        try {
            specificationService.updateUnion(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }


    @DeleteMapping("/specification/{ids}")
    public MsgBox delete(@PathVariable("ids") Long[] ids){
        try {
            specificationService.delete(ids);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

}
