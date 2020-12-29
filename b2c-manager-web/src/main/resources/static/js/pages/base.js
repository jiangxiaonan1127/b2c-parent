//初始化组件
Vue.component('paginate', VuejsPaginate);
Vue.component('v-select', VueSelect.VueSelect);
//混入对象
var base={
    data:function () {
        return{
            basePath:"",//基础路径
            entity:{},//对象的属性
            ids:[],//存储id
            pageCount:0,//总页数
            searchParam:{
                pageNum:1,
                pageSize:5
            },
            searchResult:{
                total:0,
                list:[]
            }
        }
    },
    created:function(){//构造方法
        this.findPage();
    },
    methods:{
        //方法
        findPage:function(){
            var self=this;
            // 1.路径
            //2.参数
            axios.get(this.basePath+"s",{
                params:this.searchParam
            }).then(function(response){
                self.searchResult.list=response.data.list;
                self.pageCount=Math.ceil(response.data.total/self.searchParam.pageSize);
            })
        },
        saveOrUpdate:function(){//添加 或者修改的方法
            var self=this;
            if(this.entity.id){//有ID 修改
                axios.put(this.basePath,this.entity).then(function(response){
                    console.log(response.data.code);
                    //刷新界面
                    self.findPage();
                })
            }else{//添加
                axios.post(this.basePath,this.entity).then(function(response){
                    console.log(response.data.code);
                    //刷新界面
                    self.findPage();
                })
            }

        },
        findById:function(id){
            var self=this;
            axios.get(this.basePath+"/"+id).then(function(response){
                self.entity=response.data;
            })
        },
        deleteByIds:function () {
            var self=this;
            if(this.ids.length>0){//至少选择一个
                if(confirm("确定要删除吗？")){
                    axios.delete(this.basePath+"/"+this.ids.join(",")).then(function(response){
                        console.log(response.data.code);
                        //刷新界面
                        self.findPage();
                    })
                }
            }
        },
        jsonToString : function(jstr, key) {
            var jsonArr = JSON.parse(jstr);
            var newArr = [];
            jsonArr.forEach(function(item, index, arr) {
                newArr.push(item[key]);
            });
            return newArr.join(",");
        }
    }
}