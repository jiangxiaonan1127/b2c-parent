
var contentController=new  Vue({
    mixins:[base],
    el:"#contentView", //范围
    data:{//属性
        basePath:"/portal/content",//基础路径
        contentList : [],// 广告列表
    }
    ,
    created : function() {
        this.findByCategoryId(1);
    },
    methods : {
        findByCategoryId : function(categoryId) {
            var self = this;
            var uri = "/portal/contents/" + categoryId;
            axios.get(uri).then(function(res) {
                self.contentList = res.data;
            });
        }
    }
});