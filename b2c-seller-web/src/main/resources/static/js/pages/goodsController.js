
var goodsController=new  Vue({
    mixins:[base],
    el:"#goodsView", //范围

    data:{//属性
        basePath:"/seller/itemCat",//基础路径
        pic : null,// 上传图片实体
        itemImage : {
            color : "",
            url : ""
        },
        itemCatList1 : [],// 一级分类列表
        itemCatList2 : [],// 二级分类列表
        itemCatList3 : [],// 三级分类列表
        brandList : [],// 品牌列表
        specUionList : [],// 规格选项联合实体类列表
        entity:{
            goods:{
                category1Id : 0,// 一级分类id
                category2Id : 0,// 二级分类id
                category3Id : 0,// 三级分类id
                typeTemplateId : 0,// 类型模板id
                brandId : 0// 品牌id
            },
            goodsDesc:{
                customAttributeItems : [],
                specificationItems : [],// 规格选项列表
                itemImages : [],// 图片信息列表
            },
            itemList : [],// SKU列表
        }
    },
   methods:{
        findItemCatsByParentId : function(parentId, listFlag) {
            var self = this;
            var uri = "/seller/itemCats/" + parentId;
            axios.get(uri).then(function(res) {
                self.$data[listFlag] = res.data;// $data可以用来获取vue所有的数据项
            });
        },
       findTypeTemplateByItemCatId : function(itemCatId) {
           var self = this;
           var uri = "/seller/itemCat/" + itemCatId;
           axios.get(uri).then(function(res) {
               self.entity.goods.typeTemplateId = res.data.typeId;
           });
       },
       findBrandIdsAndCustomAttributeItemsByTypeTemplateId : function(
           typeTemplateId) {
           var self = this;
           var uri = "/seller/typeTemplate/" + typeTemplateId;
           axios.get(uri).then(
               function(res) {
                   self.brandList = JSON.parse(res.data.brandIds);
                   self.entity.goodsDesc.customAttributeItems = JSON
                       .parse(res.data.customAttributeItems);
               });
       },
       findSpecUnionsByTypeTemplateId : function(typeTemplateId) {
           var self = this;
           var uri = "/seller/typeTemplate/specUnions/" + typeTemplateId;
           axios.get(uri).then(function(res) {
               // 创建specificationItems的保存结构
               self.createSpecificationItemsStructure(res);
               self.specUionList = res.data;
           });
       },
       // 创建specificationItems的保存结构
       createSpecificationItemsStructure : function(res) {
           // this.entity.goodsDesc.specificationItems = [];
           var self = this;
           res.data.forEach(function(item, index, arr) {
               // 设置响应式属性
               // 如果没有设置，则用户选择的规格信息无法更新到specificationItems中
               self.$set(self.entity.goodsDesc.specificationItems, index, {
                   attributeName : item.spec.specName,
                   attributeValue : []
               });
           });
       },
       // 创建itemList保存结构
       createItemListStructure : function() {
           var self = this;
           var structure = [ {// 初始化保存结构
               spec : {},
               price : 0,
               num : 999,
               status : "0",
               isDefault : "0"
           } ];
           this.entity.goodsDesc.specificationItems.forEach(function(item,
                                                                     index, arr) {
               // 添加spec中的列，也就是给spec增加一个键值对
               structure = self.addColumn(structure, item.attributeName,
                   item.attributeValue);
           });
           // console.debug(structure);
           this.$set(this.entity, "itemList", structure);
       },
       // 添加spec中的列
       addColumn : function(list, columnName, columnValues) {
           var newList = [];
           list.forEach(function(item, index, arr) {
               columnValues.forEach(function(columnValue, ind, ar) {
                   // 必须以JSON的方式进行克隆
                   // 直接修改item的话，会覆盖之前push进去的数据
                   var row = JSON.parse(JSON.stringify(item));
                   row.spec[columnName] = columnValue;
                   newList.push(row);
               });
           });
           // 如果不判断length，会导致createItemListStructure中的循环结束。
           if (newList.length == 0)
               return list;
           return newList;
       },
       //选择哪个图片或者文件
       selectFile : function(event) {
           this.pic = event.target.files[0];
       },
       //初始化图片
       initImage : function() {
           this.itemImage = {
               color : "",
               url : ""
           };
       },uploadImage : function() {
           var formData = new FormData();// 构造一个动态表单对象 因为用的是模态框
           var self = this;
           formData.append("pic", this.pic);
           axios.post("/seller/upload", formData, {
               headers : {
                   "Content-Type" : "multipart/form-data"
               }
           }).then(function(res) {
               console.debug(res.data.code);
               self.itemImage.url = res.data.msg;
               console.debug(self.itemImage.url);
           });
       },
       addImage : function() {
           this.entity.goodsDesc.itemImages.push(this.itemImage);
       },
       removeImage : function(index) {
           this.entity.goodsDesc.itemImages.splice(index, 1);
       },
    },created:function(){
            this.findItemCatsByParentId(0,"itemCatList1");
    },watch:{
        "entity.goods.category1Id":function(newVar,oldVar){
                this.findItemCatsByParentId(newVar,"itemCatList2");
        },"entity.goods.category2Id":function(newVar,oldVar){
            this.findItemCatsByParentId(newVar,"itemCatList3");
        },"entity.goods.category3Id":function(newVar,oldVar){
            this.findTypeTemplateByItemCatId(newVar);
        },"entity.goods.typeTemplateId":function (newVar,oldVar){
            this.findBrandIdsAndCustomAttributeItemsByTypeTemplateId(newVar);
            this.findSpecUnionsByTypeTemplateId(newVar);
        }

    }
});