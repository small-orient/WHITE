<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 博客数据
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button
                        type="primary"
                        icon="el-icon-delete"
                        class="handle-del mr10"
                        @click="delAllSelection"
                >批量删除
                </el-button>
                <!--博客类别列表下拉菜单-->
                <!--:filter-method="selectLabel"-->
                <el-select
                        v-model="typeName.typeName"
                        placeholder="类别查询"
                        filterable
                        class="handle-select mr10"
                        @change="selectLabel"
                        @keyup.enter.native="selectLabel"
                        >

                    <el-option
                            v-for="name in typeName"
                            :key="name.id"
                            :label="name.typeName"
                            :value="name.id">
                    </el-option>

                </el-select>


                <!--搜索框-->
                <el-autocomplete
                        v-model="title"
                        placeholder="搜索博客名"
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
                        style="width: 80px;text-align: center;"
                        @click="page(1,null,null)">刷新
                </el-link>
        </div>
            <!--数据表-->
            <el-table
                    :data="blogs"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
                    show-overflow-tooltip
            >
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="id" label="编号" width="55" align="center"></el-table-column>
                <el-table-column prop="nickName" label="博主" align="center"></el-table-column>
                <el-table-column prop="typeName" label="博客类别" align="center"></el-table-column>
                <el-table-column prop="title" label="博客名称" align="center"></el-table-column>
                <el-table-column prop="summary" label="摘要" align="center"></el-table-column>
                <el-table-column prop="createTime" label="发表时间" align="center"></el-table-column>
                <el-table-column prop="updateTime" label="更新时间" align="center"></el-table-column>
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
                                icon="el-icon-refresh-left"
                                @click="handleRevert(scope.$index, scope.row)"
                        >还原
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
        name: 'blogs',
        data() {
            return {
                blogs: {},//博客数据
                query: {
                    //分页信息
                    currentPage: 1,
                    pageSize: 9,
                    pageTotal: 0
                },
                typeName: {}, //类别名数组
                titles:[],//搜索框提示标题信息
                title:'', //用户输入搜索框的博客名
                state:'',



                multipleSelection: [],
                delList: [],
                form: {},
                idx: -1,
                id: -1
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
                if (!this.title) {
                    this.$message.warning('请输入您要搜索博客的名称')
                    return
                }
                let title = encodeURI(encodeURI(this.title));

                const g_this = this
                g_this.page(g_this.query.currentPage,null,title);

            },
            // 还原操作
            handleRevert(index, row) {
                this.detectRealBlogger();
                // 二次确认删除
                this.$confirm('确定要还原吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    const g_this = this
                    this.axios.get("/blog/revert/"+row.id,{
                        headers:{
                            "Authorization":localStorage.getItem("token")
                        }
                    }).then(res=>{
                        console.log(res+"还原博客，单选");

                        this.$message.success('还原成功');
                        //1s刷新页面
                        setTimeout(() =>{
                            g_this.page();
                        },500);

                    })
                }).catch(() => {
                });
            },
            // 删除操作
            handleDelete(index, row) {
                this.detectRealBlogger();
                // 二次确认删除
                this.$confirm('确定要删除吗？彻底删除后无法恢复', '提示', {
                    type: 'warning'
                }).then(() => {
                    const g_this = this
                    let ids= [{"id":row.id}];
                    this.axios.post("/blog/recycleDelete",ids,{
                        headers:{
                            "Authorization":localStorage.getItem("token")
                        }
                    }).then(res=>{
                        console.log(res+"删除博客，单选");

                        this.$message.success('删除成功');
                        //1s刷新页面
                        setTimeout(() =>{
                            g_this.page();
                        },500);

                    })
                }).catch(() => {
                });
            },
            // 多选操作
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            //多选删除
            delAllSelection() {
                this.detectRealBlogger();
                // 二次确认删除
                this.$confirm('确定要删除吗？彻底删除后无法恢复', '提示', {
                    type: 'warning'
                }).then(() => {
                const length = this.multipleSelection.length;
                let ids = [];
                this.delList = this.delList.concat(this.multipleSelection);
                for (let i = 0; i < length; i++) {
                    //拼接成{"id":row.id}类型
                    ids.push('{'+'"'+'id'+'"'+':'+this.multipleSelection[i].id+'}');
                }
                const g_this = this
                this.axios.post("/blog/recycleDelete",ids,{
                    headers:{
                        "Authorization":localStorage.getItem("token")
                    }
                }).then(res=>{
                    console.log(res+"删除博客，多选");
                    g_this.$message.success('删除成功');
                    //1s刷新页面
                    setTimeout(() =>{
                        g_this.page();
                    },500);
                })
                this.multipleSelection = [];
                }).catch(() => {
                });
            },
            // 编辑操作
            handleEdit(index, row) {
                this.idx = index;
                this.form = row;

                /*alert(row.id)*/
                //跳转至编辑页面
                this.$router.push("/blog/edit/"+row.id);

            },

            //分页
            page(currentPage,typeName_t,title_t) {
                if (!typeName_t){
                    //null作为是否有类型名的标记符，后台根据null排除含有typeName
                    typeName_t = "null";
                }
                if (!title_t){
                    //null作为是否有类型名的标记符，后台根据null排除含有title
                    title_t = "null";
                }
                const g_this = this;
                /*alert(currentPage)*/
                let data = {"currentPage":currentPage,"typeName":typeName_t,"title":title_t};
                g_this.axios.post("/blog/recycle/",data).then(res => {
                    console.log(res+"回收站分页");
                    g_this.blogs = res.data.data.records;
                    g_this.query.currentPage = res.data.data.current;
                    g_this.query.pageTotal = res.data.data.total;
                    g_this.query.pageSize = res.data.data.size;


                });
            } ,
            // 获取博客类型
            getTypeName() {
                const g_this = this;
                g_this.axios.get("/type/typeName").then(res => {
                    console.log(res+"博客类型");
                   g_this.typeName=res.data.data;

                });
            },
            //获取下拉框选项，并查出数据
            selectLabel(val){
                const g_this = this;
                let obj = {};
                obj = g_this.typeName.find((item)=>{
                    return item.id === val;//筛选出匹配数据
                });
               /* console.log(obj.typeName);
                console.log(obj.id);
                alert(obj.typeName)*/

                g_this.page(g_this.query.currentPage,obj.typeName,null)
                //如果输入框有值，则清空
                g_this.title = '';
            },
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
                this.page(1,null,null);
                //获取博客类型
                this.getTypeName();
            }
        },
        mounted() {
            //这是搜索框显示提示信息的，没有实现
            for (let i = 0; i <this.blogs.length ; i++) {
                this.titles.push(this.blogs[i].title);
            }


        },
        /*//检测路由变化，刷新跳转的路由组件
        watch: {
            '$route' (to, from) {
                this.$router.go(0);
            }
        },*/

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
    .table-td-thumb {
        display: block;
        margin: auto;
        width: 40px;
        height: 40px;
    }
</style>
