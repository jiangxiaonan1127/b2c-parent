package com.turing.b2c.model.pojo.union;

import com.turing.b2c.model.pojo.Goods;
import com.turing.b2c.model.pojo.GoodsDesc;
import com.turing.b2c.model.pojo.Item;

import java.io.Serializable;
import java.util.List;

public class GoodsUnion implements Serializable{
    private static final long serialVersionUID = 1866768136311892465L;
    private Goods goods;
    private GoodsDesc goodsDesc;
    private List<Item> itemList;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(GoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public GoodsUnion(Goods goods, GoodsDesc goodsDesc, List<Item> itemList) {
        this.goods = goods;
        this.goodsDesc = goodsDesc;
        this.itemList = itemList;
    }

    public GoodsUnion() {
    }
}
