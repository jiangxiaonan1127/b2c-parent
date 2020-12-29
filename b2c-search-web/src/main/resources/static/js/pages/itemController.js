
var itemController=new  Vue({
    el:"#itemView", //范围
    data:{//属性
        basePath:"/search/item",//基础路径
        pageCount:0,//总页数
        entity:{},
        searchParam : {
            pageNum : 1,
            pageSize : 10,
            keyword : "",// 关键字
            category : "",//商品分类
            brand : "",//品牌
            spec : {}   //规格
        },
        searchResult : {
            total : 0,
            list : [],
            categoryList : [],// 商品类型列表
            brandList : [],// 品牌列表
            specUnionList : []// 规格列表
        }
    },
    created:function(){//构造方法
        this.findPage();
    },
    methods: {
        //方法
        findPage: function () {
            var self = this;
            // 1.路径
            //2.参数
            axios.get(this.basePath + "s", {
                params: this.searchParam
            }).then(function (response) {
                self.searchResult = response.data;
                self.pageCount = Math.ceil(response.data.total / self.searchParam.pageSize);
            })
        },
        addSearchParam : function(key, value) {
            if (key == "category" || key == "brand") {
                this.searchParam[key] = value;
            } else {
                this.$set(this.searchParam.spec, key, value);
            }
            this.findPage();
        },
        removeSearchParam : function(key) {
            if (key == "category" || key == "brand") {
                this.searchParam[key] = "";
            } else {
                this.$delete(this.searchParam.spec, key);
            }
            this.findPage();
        },
    }
});