<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 评论信息
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div  style="background-color: rgba(246,246,246,0.8)">
            <!--标签头-->
            <template>
                <el-tabs v-model="activeName" @tab-click="handleClick" >
                    <!--删除链接-->
                    <el-tab-pane label="删除链接" name="first">
                        <!--链接表-->
                        <el-table
                                :data="linkList"
                                border
                                class="table"
                                ref="multipleTable"
                                header-cell-class-name="table-header"
                                show-overflow-tooltip
                        >
                            <el-table-column type="selection" width="55" align="center"></el-table-column>
                            <el-table-column prop="id" label="编号" width="55" align="center"></el-table-column>
                            <el-table-column prop="linkUrl" label="链接地址" align="center"></el-table-column>
                            <el-table-column prop="linkName" label="链接名称" align="center"></el-table-column>
                            <el-table-column prop="linkImg" label="图片地址" align="center"></el-table-column>
                            <el-table-column prop="createTime" label="添加时间" align="center"></el-table-column>
                            <el-table-column label="操作" width="180" align="center">
                                <template slot-scope="scope">
                                    <el-button
                                            type="text"
                                            icon="el-icon-delete"
                                            class="red"
                                            @click="deleteLink(scope.$index, scope.row)"
                                    >删除
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>
                    <!--添加链接-->
                    <el-tab-pane label="添加链接" name="second">
                        <el-form class="pwd-form animated fadeInRight">
                            <el-form-item  style="margin-bottom: 40px;margin-top: 40px">
                                <el-button size="mini" type="primary"
                                >博客名称</el-button><el-input
                                    class="input-ph"
                                    v-model="links.linkName"
                                    placeholder="添加博客名称..."></el-input>
                            </el-form-item>
                            <div class="dividing-f-pwd"></div>
                            <el-form-item  style="margin-bottom: 40px;margin-top: 40px">
                                <el-button size="mini" type="primary"
                                >博客地址</el-button><el-input
                                    class="input-ph"
                                    v-model="links.linkUrl"
                                    placeholder="添加博客地址..."></el-input>
                            </el-form-item>
                            <div class="dividing-f-pwd"></div>
                            <el-form-item  style="margin-bottom: 40px;margin-top: 40px">
                                <el-button size="mini" type="primary"
                                >图片地址</el-button><el-input
                                    class="input-ph"
                                    v-model="links.linkImg"
                                    placeholder="添加图片地址..."></el-input>
                            </el-form-item>
                            <div class="dividing-f-pwd"></div>
                            <el-form-item size="large">
                                <el-button
                                        style="margin-top: 40px;border-radius: 40px;margin-bottom: 300px"
                                        type="success"
                                        @click="submitLink()"
                                >确认添加</el-button>
                            </el-form-item>
                        </el-form>
                    </el-tab-pane>

                </el-tabs>
            </template>


        </div>


    </div>
</template>

<script>
    export default {
        name: 'blogTypes',
        data() {
            return {
                activeName:'first',
                //添加链接存放处
                links:{
                    linkName:'',
                    linkUrl:'',
                    linkImg:'',
                },
                //删除链接
                linkList:{}
            };
        },

        methods: {
            // 删除链接
            deleteLink(index, row) {
                // 二次确认删除
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    const g_this = this
                    this.axios.get("/link/delete/"+row.id,{
                        headers:{
                            "Authorization":localStorage.getItem("token")
                        }
                    }).then(res=>{
                        alert(row.id)
                        this.$message.success('删除成功');
                        //刷新页面
                        g_this.getLinks();
                    })

                }).catch(() => {
                });
            },
            //添加友情链接
            submitLink(){
                const g_this = this;
                if (this.$store.getters.getUser.id === 1){
                    g_this.axios.post('/link/add', g_this.links,{
                        headers: {
                            "Authorization": localStorage.getItem("token")
                        }
                    }).then(res => {
                        this.$message.success('添加成功');
                        g_this.links.linkName = ''
                        g_this.links.linkUrl= ''
                        g_this.links.linkImg= ''
                        //刷新页面
                        g_this.getLinks();
                    })
                }else {
                    this.$message.error('您无权限访问！');
                    this.$router.push("/fblog");
                }
            },
            getLinks(){
                const g_this = this;
                //回显链接信息
                g_this.axios.get("/link/list",{
                    headers:{
                        "Authorization":localStorage.getItem("token")}
                }).then((res) => {
                    g_this.linkList = res.data.data;
                })
            }
        },
        //开始渲染页面时直接调用分页方法渲染数据
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
                g_this.getLinks();
            }
        }
    };
</script>

<style scoped>
    @media screen and (max-width: 1280px){
        .pwd-form{
            margin-left: 18%
        }
        .dividing-f-pwd {
            width: 70%;
            line-height: 50px;
            font-size: 20px;
            color: #fff;
            border-bottom: 1px solid #ddd;
        }

    }
    @media screen and (max-width: 960px){
        .pwd-form{
            margin-left: 10%
        }
        .dividing-f-pwd {
            width: 365px;
            line-height: 50px;
            font-size: 20px;
            color: #fff;
            border-bottom: 1px solid #ddd;
        }

    }
    @media screen and (min-width: 1280px){
        .pwd-form{
            margin-left: 25%
        }
        .dividing-f-pwd {
            width: 70%;
            line-height: 50px;
            font-size: 20px;
            color: #fff;
            border-bottom: 1px solid #ddd;
        }
    }

    .input-ph{
        width: 250px;
        margin-left: 40px;
    }


</style>
