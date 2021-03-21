<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 基础表格
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-link
                    :underline="false"
                    icon="el-icon-refresh"
                    class="mrl-type"
                    @click="page(1)">刷新
            </el-link>
            <!--数据表-->
            <el-table
                    :data="blogTypes"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="id" label="编号" width="55" align="center"></el-table-column>
                <el-table-column prop="typeName" label="博客类别" align="center"></el-table-column>
                <el-table-column prop="blogNumber" label="博客数(篇)" align="center"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                @click="handleEdit(scope.$index, scope.row)"
                        >编辑
                        </el-button>
                        <el-button
                                type="text"
                                icon="el-icon-delete"
                                class="red"
                                @click="handleDelete(scope.$index, scope.row)"
                        >删除
                        </el-button>
                    </template>
                </el-table-column>

            </el-table>
            <!--分页栏-->
            <!--@current-change=page 这条属性就是点击页码进行分页，page是自定义的分页方法-->
            <div class="pagination">
                <el-pagination
                        background
                        layout="total, prev, pager, next"
                        :current-page="query.currentPage"
                        :page-size="query.pageSize"
                        :total="query.pageTotal"
                        @current-change=page
                ></el-pagination>
            </div>

        </div>

    </div>
</template>

<script>
export default {
    name: 'blogTypes',
    data() {
        return {
            blogTypes: {}, //博客类别数据
            query: {
                //分页信息
                currentPage: 1,
                pageSize: 9,
                pageTotal: 0
            },
            multipleSelection: [],
            delList: [],
            form: {},
            idx: -1,
            id: -1,

            /*//抽屉
            drawer: false,
            direction: 'rtl',//右边打开*/
        };
    },

    methods: {


        // 分页导航
        page(currentPage) {
            const g_this = this;
            //post，验证登录权限，防止直接使用接口进行访问
            // g_this.axios.get("/type/pagination?currentPage=" + currentPage).then(res =>
          g_this.axios.post("/type/pagination",currentPage,{
                    headers:{
                        "Authorization":localStorage.getItem("token")
                    }
                }).then(res => {
                console.log(res+"博客类别分页");
                g_this.blogTypes = res.data.data.records;
                g_this.query.currentPage = res.data.data.current;
                g_this.query.pageTotal = res.data.data.total;
                g_this.query.pageSize = res.data.data.size;

            });
        },
        // 删除操作
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('删除 \"'+row.typeName+'\" 类型，该类型下 '+row.blogNumber+' 篇博客将同时删除！ 确定要删除吗？', '提示', {
                type: 'warning'
            }).then(() => {
                const g_this = this
                let ids= [{"id":row.id}];
                this.axios.post("/type/blogType/delete",ids,{
                    headers:{
                        "Authorization":localStorage.getItem("token")
                    }
                }).then(res=>{
                    console.log(res+"删除博客类别，单选");

                    this.$message.success('删除成功');
                    //1s刷新页面
                    setTimeout(() =>{
                        g_this.page(1);
                    },500);

                })
            }).catch(() => {
            });
        },
        // 多选操作
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },

        // 编辑操作
        handleEdit(index, row) {
            this.idx = index;
            this.form = row;

            /*alert(row.id)*/
            //跳转至编辑页面
            this.$router.push("/blog/"+row.id+"/edit");
        },
    },
    //开始渲染页面时直接调用分页方法渲染数据
    created() {

        let token = localStorage.getItem("token");
        let userInfo = window.sessionStorage.getItem("userInfo");

        if (token===null || token ===undefined ||token===''){
            this.$message.error('您无权限访问！');
            this.$router.push("/fblog");
            location.reload();
        }else if (userInfo===null || userInfo ===undefined ||userInfo===''){
            this.$message.error('您无权限访问！');
            this.$router.push("/fblog");
            location.reload();
        }else if (this.$store.getters.getUser.id !== 1){
            //代表未登录，移除因直接关闭浏览器残留的token
            window.localStorage.removeItem("token");
            this.$message.error('您无权限访问！');
            this.$router.push("/fblog");
            location.reload();
        }else{
            //开始渲染页面时直接调用分页方法渲染数据
            this.page(1,null,null);
            //获取博客类型
            this.getTypeName();
        }

    }


};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 120px;
}

.handle-input {
    width: 300px;
    display: inline-block;
}
.table {
    width: 100%;
    font-size: 14px;
}
.red {
    color: #ff0000;
}
.container{
    background:rgba(255,255,255,0.7);
}
.mr10 {
    margin-right: 10px;
    margin-left: 10px;
}
.mrl-type{
    margin-bottom: 20px;
}
.table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
}
</style>
