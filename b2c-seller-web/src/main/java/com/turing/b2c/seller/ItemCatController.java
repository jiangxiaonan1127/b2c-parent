package com.turing.b2c.seller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.turing.b2c.model.dto.MsgBox;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.ItemCat;
import com.turing.b2c.sellergoods.ItemCatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    @GetMapping("/itemCat")
    public List<ItemCat> findAll(){
        return itemCatService.findAll();
    }
    //分页查询所有数据
    @GetMapping("/s")
    public SearchResult<ItemCat> findAll(SearchParam searchParam){
        return itemCatService.findPage(searchParam);
    }

    //查询单个
    @GetMapping("/itemCat/{id}")
    public ItemCat findById(@PathVariable("id") Long id){
        return itemCatService.findById(id);
    }
    @GetMapping("/itemCats/{parentId}")
    public List<ItemCat> findByParentId(@PathVariable("parentId") Long parentId){
        return itemCatService.findByParentId(parentId);
    }

    //增加
    @PostMapping("/itemCat")
    public MsgBox save(@RequestBody ItemCat entity){
        try {
            itemCatService.save(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

    @PutMapping("/itemCat")
    public MsgBox update(@RequestBody ItemCat entity){
        try {
            itemCatService.update(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }


    @DeleteMapping("/itemCat/{ids}")
    public MsgBox delete(@PathVariable("ids") Long[] ids){
        try {
            itemCatService.delete(ids);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

}
