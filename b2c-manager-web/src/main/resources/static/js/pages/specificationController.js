
var specificationController=new  Vue({
    mixins:[base],
    el:"#specificationView", //范围
    data:{//属性
        basePath:"/manager/specification"//基础路径

    },created:function(){
        this.$set(this.entity,"spec",{});
        this.$set(this.entity,"specOptionList",[])
    },methods:{
        clearRows:function(){
            this.entity.spec={};
            this.entity.specOptionList=[];
        },addRow:function () {
            this.entity.specOptionList.push({});
        },deleteRow:function (index) {
            this.entity.specOptionList.splice(index,1);
        },saveOrUpdate:function(){
            var self=this;
            if(this.entity.spec.id){//有ID修改
                axios.put(this.basePath,this.entity).then(function(response){
                    console.log(response.data.msg);
                    //刷新界面
                    self.findPage();
                })
            }else{
                axios.post(this.basePath,this.entity).then(function(response){
                    console.log(response.data.msg);
                    //刷新界面
                    self.findPage();
                })
            }
        }
    }
});