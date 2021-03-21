<template>
    <div class="blogs">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 还原用户
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <!--搜索框-->
                <el-autocomplete
                        v-model="nameInfo"
                        placeholder="搜索用户名或昵称..."
                        class="handle-input mr10"
                        :fetch-suggestions="querySearch"
                        :trigger-on-focus="true"
                        @select="handleSelect"
                        @keyup.enter.native="Search"
                ></el-autocomplete>
                <el-button type="primary" icon="el-icon-search" @click="Search">搜索</el-button>
                <el-link
                        :underline="false"
                        icon="el-icon-refresh"
                        class="mrl"
                        @click="page(1,null,1)">刷新
                </el-link>
            </div>
            <!--数据表-->
            <el-table
                    :data="users"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    show-overflow-tooltip
            >
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="id" label="用户id" width="55" align="center"></el-table-column>
                <el-table-column prop="userName" label="用户名" align="center"></el-table-column>
                <el-table-column prop="nickName" label="昵称" align="center"></el-table-column>
                <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
                <el-table-column prop="phone" label="手机" align="center"></el-table-column>
                <el-table-column prop="loginTime" label="最近登录时间" align="center"></el-table-column>
                <el-table-column prop="createTime" label="用户创建时间" align="center"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-unlock"
                                @click="handleReduce(scope.$index, scope.row)"
                        >账户解冻
                        </el-button>
                    </template>

                </el-table-column>

            </el-table>
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
        name: 'blogs',
        data() {
            return {
                nameInfo:'',//根据昵称查询用户
                users:{},//用户信息回s显
                query: {
                    //分页信息
                    currentPage: 1,
                    pageSize: 9,
                    pageTotal: 0
                },

            };
        },

        methods: {
            /* fetch-suggestions 是一个返回输入建议的方法属性，在该方法中你可以在输入建议数据准备好时通过 cb(data) 返回到 autocomplete 组件中. */
            querySearch (queryString, cb) {
                const titles = this.titles
                const results = queryString ? titles.filter(this.createFilter(queryString)) :titles;
                // 调用 callback 返回建议列表的数据
                cb(results)
            },
            createFilter (queryString) {
                return state => {
                    return (
                        state.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1  // 改为===0 就是筛选的数据只是首字匹配的列表项，需要包含输入字的所有列表项改为！==-1
                    )
                };
            },
            handleSelect(item) {
                console.log(item);
            },

            // 触发搜索按钮
            Search() {
                if (!this.nameInfo) {
                    this.$message.warning('请输入您要搜索用户的信息')
                    return
                }
                const g_this = this
                g_this.page(g_this.query.currentPage, g_this.nameInfo);

            },

            //解冻账户
            handleReduce(index, row){
                this.detectRealBlogger();
                const g_this = this;
                const id= row.id;
                const locked= 0;
                g_this.axios.get("/user/admin/locked/"+id+"/"+locked,{
                    headers:{
                        "Authorization":localStorage.getItem("token")
                    }
                }).then(res => {
                    console.log(res+"解冻账户");
                    g_this.$message.success("操作成功");
                    g_this.page(g_this.query.currentPage, null,1 );
                });
            },

            //分页
            page(currentPage,nameInfo,locked) {
                this.detectRealBlogger();
                //给默认值
                if (!nameInfo){
                    //null字符串作为是否有类型名的标记符
                    nameInfo = "null";
                }
                if (!locked){
                    locked = 1;
                }
                const g_this = this;
                let data = {"currentPage":currentPage,"nameInfo":nameInfo,"locked":locked};
                g_this.axios.post("/user/admin/user",data,{
                    headers:{
                        "Authorization":localStorage.getItem("token")
                    }
                }).then(res => {
                    console.log(res+"用户管理分页");
                    g_this.users = res.data.data.records;
                    g_this.query.currentPage = res.data.data.current;
                    g_this.query.pageTotal = res.data.data.total;
                    g_this.query.pageSize = res.data.data.size;
                });
            } ,
            //判断此时是否是真正博主在登录
            detectRealBlogger(){
                //先判断此时是否是用户强行关闭浏览器，通过残留token强行进入的接口
                let userInfo = window.sessionStorage.getItem("userInfo");
                if (userInfo===null || userInfo ===undefined ||userInfo===''){
                    window.localStorage.removeItem("token");//移除
                    this.$router.push("/fblog");
                    location.reload();
                }
            }
        },

        created() {
            let token = localStorage.getItem("token");
            let userInfo = window.sessionStorage.getItem("userInfo");
            const g_this = this;
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
                this.page(1,null,1);
            }

        },


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

    .mr10 {
        margin-right: 10px;
    }
    .mrl{
        margin-left:1%;
    }

    .container{
        background:rgba(255,255,255,0.7);
    }
</style>
