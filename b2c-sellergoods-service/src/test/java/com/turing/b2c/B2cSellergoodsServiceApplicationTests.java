package com.turing.b2c;

import com.turing.b2c.model.dto.SearchParam;
import com.turing.b2c.model.pojo.Goods;
import com.turing.b2c.model.pojo.TypeTemplate;
import com.turing.b2c.sellergoods.ItemCatService;
import com.turing.b2c.sellergoods.TypeTemplateService;
import com.turing.b2c.sellergoods.GoodsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {B2cSellergoodsServiceApplication.class})
class B2cSellergoodsServiceApplicationTests {
	@Autowired
	private ItemCatService itemCatService;
	@Autowired
	private TypeTemplateService typeTemplateService;
	@Autowired
	private GoodsService goodsService;
	@Test
	void testCacheTypeTemplateId() {
		itemCatService.findByParentId(0L);
	}

	@Test
	void testCacheBrandListAndSpecUnionList() {
		SearchParam searchParam = new SearchParam(1, 4);
		typeTemplateService.findPage(searchParam);
	}

	@Test
	void testSendUpdateMsg(){
		List<Goods> goodsList = goodsService.findAll();
		Long [] ids = new Long[goodsList.size()];
		for (int i=0;i<goodsList.size();i++){
			ids[i] = goodsList.get(i).getId();
		}
		goodsService.updateAuditStatus(ids,"1");
	}
}
