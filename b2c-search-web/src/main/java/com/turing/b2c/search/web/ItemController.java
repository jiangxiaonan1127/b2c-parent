package com.turing.b2c.search.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.turing.b2c.model.dto.MsgBox;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Item;
import com.turing.b2c.search.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class ItemController {

    @Reference
    private ItemService ItemService;

    @GetMapping("/item")
    public List<Item> findAll(){
        return ItemService.findAll();
    }
    //分页查询所有数据
    @GetMapping("/items")
    public SearchResult<Item> findPage(SearchParam searchParam){
        return ItemService.findPage(searchParam);
    }

    //查询单个
    @GetMapping("/item/{id}")
    public Item findById(@PathVariable("id") Long id){
        return ItemService.findById(id);
    }

    //增加
    @PostMapping("/item")
    public MsgBox save(@RequestBody Item entity){
        try {
            ItemService.save(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

    @PutMapping("/item")
    public MsgBox update(@RequestBody Item entity){
        try {
            ItemService.update(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }


    @DeleteMapping("/item/{ids}")
    public MsgBox delete(@PathVariable("ids") Long[] ids){
        try {
            ItemService.delete(ids);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

}
