package com.turing.b2c.portal.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.turing.b2c.content.ContentService;
import com.turing.b2c.model.dto.MsgBox;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Content;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portal")
public class ContentController {

    @Reference
    private ContentService contentService;

    @GetMapping("/content")
    public List<Content> findAll(){
        return contentService.findAll();
    }
    //分页查询所有数据
    @GetMapping("/contents")
    public SearchResult<Content> findAll(SearchParam searchParam){
        return contentService.findPage(searchParam);
    }

    //查询单个
    @GetMapping("/content/{id}")
    public Content findById(@PathVariable("id") Long id){
        return contentService.findById(id);
    }
    @GetMapping("/contents/{categoryId}")
    public List<Content> findByCategoryId(@PathVariable("categoryId") Long categoryId){
        return contentService.findByCategoryId(categoryId);
    }


    //增加
    @PostMapping("/content")
    public MsgBox save(@RequestBody Content entity){
        try {
            contentService.save(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

    @PutMapping("/content")
    public MsgBox update(@RequestBody Content entity){
        try {
            contentService.update(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }


    @DeleteMapping("/content/{ids}")
    public MsgBox delete(@PathVariable("ids") Long[] ids){
        try {
            contentService.delete(ids);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

}
