
var typeTemplateController=new  Vue({
    mixins:[base],
    el:"#typeTemplateView", //范围
    data:{//属性
        basePath:"/manager/typeTemplate",//基础路径
        brandList:[],
        specList:[]
    },created:function () {
        this.$set(this.entity, "customAttributeItems", []);
        this.findAllBrands();
        this.findAllSpec();
    },methods:{
        initSelectAndRows : function() {
            this.entity.id = null;
            this.entity.name = "";
            this.entity.specIds = [];
            this.entity.brandIds = [];
            this.entity.customAttributeItems = [];
        },
        addRow:function () {
            this.entity.customAttributeItems.push({});
        },
        deleteRow:function (index) {
            this.entity.customAttributeItems.splice(index,1);
        },
        findAllBrands:function () {
            var self=this;
            axios.get("/manager/brand").then(function (res) {
                self.brandList=res.data;
            })
        },
        findAllSpec:function () {
            var self=this;
            axios.get("/manager/specification").then(function (res) {
                self.specList=res.data;
            })
        }
        ,findById:function(id){
            var self=this;
            axios.get(this.basePath+"/"+id).then(function (res) {

                self.entity=res.data;
                self.entity.brandIds=JSON
                    .parse(self.entity.brandIds);
                self.entity.specIds=JSON
                    .parse(self.entity.specIds);
                self.entity.customAttributeItems=JSON
                    .parse(self.entity.customAttributeItems);
            })
        }

    }
});