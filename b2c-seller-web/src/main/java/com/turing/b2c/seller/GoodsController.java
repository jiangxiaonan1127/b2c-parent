package com.turing.b2c.seller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.turing.b2c.model.dto.MsgBox;
import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.dto.SearchResult;
import com.turing.b2c.model.pojo.Goods;
import com.turing.b2c.sellergoods.GoodsService;
import com.turing.b2c.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class GoodsController {

    @Reference
    private GoodsService goodsService;


    @Value("${file.server.path}")
    private String fileServerPath;

    @GetMapping("/goods")
    public List<Goods> findAll(){
        return goodsService.findAll();
    }
    //分页查询所有数据
    @GetMapping("/goodss")
    public SearchResult<Goods> findAll(SearchParam searchParam){
        return goodsService.findPage(searchParam);
    }

    //查询单个
    @GetMapping("/goods/{id}")
    public Goods findById(@PathVariable("id") Long id){
        return goodsService.findById(id);
    }

    //增加
    @PostMapping("/goods")
    public MsgBox save(@RequestBody Goods entity){
        try {
            goodsService.save(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }

    @PutMapping("/goods")
    public MsgBox update(@RequestBody Goods entity){
        try {
            goodsService.update(entity);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }


    @DeleteMapping("/goods/{ids}")
    public MsgBox delete(@PathVariable("ids") Long[] ids){
        try {
            goodsService.delete(ids);
            return new MsgBox(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"操作失败");
        }
    }


    @PostMapping("/upload")
    public MsgBox upload(@RequestParam("pic") MultipartFile pic){
        //调用上传图片的方法
        try {
            FastDFSClient fastDFSClient=new FastDFSClient("fdfs_client.conf");
            String name=fastDFSClient.upload(pic.getBytes(),"jpg");
            return new MsgBox(true,fileServerPath+name);
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgBox(false,"图片上传失败");
        }
    }
}
