<template>
    <div class="blog-type-content">
                <!--头部导航栏-->
        <!--头部导航栏-->
        <div style="margin: 20px 3%;" class="card-top">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">
                    <i style="color: rgb(234,236,241)">首页</i>
                </el-breadcrumb-item>
                <el-breadcrumb-item :to="{ path: '/ftype' }">
                    <i style="color: rgb(234,236,241)">文章</i></el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-row :gutter="10" >
            <el-col  :xs="24" :sm="24" :md="16" :lg="16" :xl="16">
                <div class="grid-content m-frame">
                    <!--左边栏外框-->
                    <div class="block" >
                        <el-timeline class="card-blogs" style="min-height: 800px">
                            <el-timeline-item :timestamp="blog.createTime"
                                              placement="top"
                                              v-for="blog in blogs"

                            >
                                <el-card style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)"
                                         class="blog-card  animated fadeInLeft" >
                                    <el-row :gutter="20">
                                        <el-col :span="4">
                                            <div class="grid-content ">
                                                <el-image
                                                        :src="'https://www.blog.baidetu.cn'+blog.blogImg"
                                                        style="max-width: 100px; max-height: 100px;;min-width: 50px;min-height: 50px"
                                                        fit="fill"
                                                        v-show="imgDisplay">
                                                </el-image>
                                            </div>
                                        </el-col>
                                        <el-col :span="20">
                                            <!--博客数据-->
                                            <div class="blog-type-title ">
                                                <h3>
                                                    <router-link :to="{name:'FBlogDetail',params:{blogId:blog.id}}">
                                                        <span style="color: rgb(58,62,74)">{{blog.title}}</span>
                                                    </router-link>
                                                </h3>
                                                <span>{{blog.summary.substring(0,23)+"..."}}</span>
                                            </div>
                                        </el-col>
                                    </el-row>
                                    <el-row :gutter="20" >
                                        <el-col :span="15">
                                            <div class="grid-content">
                                                <div style="margin-top: 12px">

                                                    <span class="el-icon-s-custom word-color" >&nbsp;{{blog.nickName}}</span>&nbsp;
                                                    <span class="el-icon-document-checked word-color">&nbsp;{{blog.typeName}}</span>&nbsp;
                                                    <span class="el-icon-discount word-color">&nbsp;
                                                                    <span v-for="keyword in blog.keyword">
                                                                         <el-link
                                                                                  style="padding-bottom: 4px"
                                                                                  @click="keywordFindBlogs(keyword)"
                                                                         >{{keyword}}&nbsp;</el-link>
                                                                    </span>
                                                                </span>&nbsp;

                                                </div>
                                            </div>
                                        </el-col>
                                        <el-col :span="9">
                                            <div class="grid-content">
                                                <div style="margin-top: 12px">
                                                    <span class="el-icon-chat-line-round word-color">&nbsp;{{blog.commentHit}}</span>&nbsp;
                                                    <span class="el-icon-view el-icon--right word-color">&nbsp;{{blog.clickHit}}</span>
                                                </div>
                                            </div>
                                        </el-col>
                                    </el-row>
                                </el-card>
                            </el-timeline-item>
                        </el-timeline>
                    </div>


                    <!--分页-->
                    <div class="pagination-blogs" style="text-align: center">
                        <el-pagination
                                background
                                layout="prev, pager, next"
                                :current-page="query.currentPage"
                                :page-size="query.pageSize"
                                :total="query.pageTotal"
                                @current-change=f_t_page
                        ></el-pagination>
                    </div>
                </div>

            </el-col>
            <el-col  :xs="24" :sm="24" :md="8" :lg="8" :xl="8" style="margin-top: 22px">
                <div class=" animated fadeInRight">
                <!--搜索框-->
                <div style="margin-top: 25px;">
                    <div class="m_f_search" style="margin-bottom: 15px;" >
                            <el-input
                                    placeholder="请输入关键词搜索"
                                    @keyup.enter.native="typeSearch"
                                    v-model="input">
                                <el-button slot="append"
                                           icon="el-icon-search"
                                            @click="typeSearch"
                                ></el-button>
                            </el-input>
                    </div>
                </div>

                <div class=" m-frame-right">
                    <!--类别-->
                    <el-card class="card card-right">
                        <div slot="header" class="clearfix">
                            <span style="color: cornflowerblue" class="el-icon-guide">&nbsp;分类导航</span>
                        </div>
                        <!--导航遍历-->
                            <el-row>
                                <el-col :span="8" v-for="(type, index) in fBlogTypes"
                                        :key="index"
                                        v-if="index < 18">
                                        <el-card
                                                :body-style="{ padding: '0px' }"
                                                style="margin:3%;box-shadow: 0 2px 4px rgba(0, 0, 0, .01), 0 0 6px rgba(0, 0, 0, .01)"
                                        >
                                            <div style="padding: 10px;text-align: center">
                                                <el-link :underline="false"
                                                         @click="f_t_page(1,type.typeName,null)">
                                                    {{type.typeName}}
                                                </el-link>
                                            </div>
                                        </el-card>
                                </el-col>
                            </el-row>


                    </el-card>
                </div>

                <div class="  m-frame-right">
                    <div class="m_f_flag">
                        <!--推荐-->
                        <el-card class="card card-right" >
                            <div slot="header" class="clearfix">
                                <!--通过svg引入显示彩色-->
                                <span class="iconfont icon-dianzan2" style="color: cornflowerblue">&nbsp;推荐</span>
                            </div>
                            <div v-for="(hotblog,index) in thotBlogs"
                                 :key="index" v-if="index < 9"
                                 class="text item">
                                    <el-button type="danger" v-if="index < 2">{{index+1}}</el-button>
                                    <el-button type="warning" v-else-if="index < 6">{{index+1}}</el-button>
                                    <el-button type="primary" v-else>{{index+1}}</el-button>
                                    <router-link :to="{name:'FBlogDetail',params:{blogId:hotblog.id}}">
                                        <i>&nbsp;&nbsp;</i>
                                        <span style="color: rgb(86,132,238)">
                                            {{hotblog.title.substring(0,15)}}</span>
                                    </router-link>
                                    <span class="clickHit-blogs"
                                          style="margin-left:  3%;color: #f38006">{{hotblog.clickHit}}阅</span>
                            </div>
                        </el-card></div>
                </div>

                <div class=" m-frame-right">
                    <div class="m_f_flag">
                        <!--标签-->
                        <el-card class="card  card-right"  >
                            <div slot="header" class="clearfix">
                                <!--通过svg引入显示彩色-->
                                <span class="el-icon-position"
                                      style="color: cornflowerblue">&nbsp;标签</span>
                            </div>
                            <span v-for="(sign,index) in tblogKeywords"
                                  :key="index" class="text item">
                                  <el-link style="padding:10px;" @click="keywordSearch(sign.id)">
                                        <span style="color: rgba(64,158,255,1)" >{{sign.keyword}}</span>
                                </el-link>
                            </span>
                        </el-card>
                    </div>
                </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "FblogType",
        data() {
            return {
                blogs: {},//博客数据
                query: {
                    //分页信息
                    currentPage: 1,
                    pageSize: 9,
                    pageTotal: 0
                },
                thotBlogs:{}, //热文博客集合
                tblogKeywords:[], //博客标签数组
                fBlogTypes:{}, //博客分类
                input:'',  //根据搜索内容搜索博客
                flag:'', //根据博客标签搜索博客

                imgDisplay:true, //控制显示博客封面图片

            }
        },
        methods:{
            //分页
            f_t_page(currentPage,typeName_t,title_t) {
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
                g_this.axios.post("/blog/pagination/",data).then(res => {
                   /* console.log(res+"类别页面分页");*/
                    g_this.blogs = res.data.data.records;
                    g_this.query.currentPage = res.data.data.current;
                    g_this.query.pageTotal = res.data.data.total;
                    g_this.query.pageSize = res.data.data.size;

                });
            } ,
            //查询博客
            typeSearch(){
                const g_this = this;
                g_this.f_t_page(1,null,g_this.input);
            },
            //根据标签关键字ID查询博客
            keywordSearch(keywordId){
                const g_this = this;
                this.axios.get("/keyword/getBlogs/"+keywordId).then(res => {
                    //重新设置分页信息
                    g_this.blogs = res.data.data.records;
                    g_this.query.currentPage = res.data.data.current;
                    g_this.query.pageTotal = res.data.data.total;
                    g_this.query.pageSize = res.data.data.size;
                });
            },
            //根据标签关键字查询博客
            keywordFindBlogs(keyword){
                const g_this = this;
                this.axios.post("/keyword/getBlogs/keyword",keyword).then(res => {
                    //重新设置分页信息
                    g_this.blogs = res.data.data.records;
                    g_this.query.currentPage = res.data.data.current;
                    g_this.query.pageTotal = res.data.data.total;
                    g_this.query.pageSize = res.data.data.size;
                });
            }
        },
        created() {
            //开始渲染页面时直接调用分页方法渲染数据
            this.f_t_page(1,null,null);
            const g_this = this;
            //获取分类
            g_this.axios.get("/type/typeName").then(res =>{
                g_this.fBlogTypes = res.data.data;
            })

            //获取热文博客
            this.axios.get("/blog/hot").then(res =>{
                g_this.thotBlogs = res.data.data;
            })

            //获取标签
            this.axios.get("/keyword/get").then(res =>{
                g_this.tblogKeywords = res.data.data;
            })

        },
    }
</script>

<style scoped>

    /*屏幕小于992px则显示大小为960px,
     card属性距离边框左右7%,
     搜索框隐藏，设置none，代表去除元素，底下元素上移*/
    @media screen and (max-width: 992px){
        .blog-type-content{
            max-width: 960px;
            margin: 1px auto;

        }
        .card{
            margin:8% 7%;
            border-radius: 10px;
            background:rgba(255,255,255,0.85);

        }
        .card-blogs{
            margin:20px 9%;
            border-radius: 10px;
        }
        .card-top{
            margin:0 7%;
            border-radius: 10px;
        }
        .m_f_search{
            display:none;
        }
        .m-frame-right{
            display:none;
        }
        .blog-card{
            border-radius: 5px;
            background-color: rgba(255,255,255,0.85);
        }
        .card-right{
            margin:10% 12%;
            border-radius: 8px;
            background-color: rgba(255, 255, 255, 0.9);
        }
        .title-blogs{
            font-size: 20px;
            color: rgb(86,132,238)
        }
        .clickHit-blogs{
            font-size: 15px;
            color: #3b3f4b;
        }
        .blog-type-title{
            margin-left: 13%
        }

    }


    @media screen and (min-width: 992px){
        .blog-type-content{
            max-width: 1080px;
            margin: 1px auto;
        }
        .card{
            border-radius: 10px;
            padding-left:5%;
            padding-right:5%;
            background:rgba(255,255,255,0.85);
        }
        .card-top{
            border-radius: 10px;
            padding-left:5%;
            padding-right:5%;
        }
        .blog-card{
            border-radius: 5px;
            background-color: rgba(255,255,255,0.85);
        }
        .card-blogs{
            margin:0 5%;
            border-radius: 10px;
        }
        .m-frame-right{
            margin-top: 30px ;
            margin-bottom: 30px;
            padding-left: 2%;
            padding-right: 2%;
        }
        .blog-type-title{
            margin-left: 5%
        }

    }

    /*各个栏边框距离调整*/
    .m-frame{
        margin:30px auto;
    }


    .el-select .el-input {
        width: 130px;
    }
    .input-with-select .el-input-group__prepend {
        background-color: #fff;
    }

    .f_pagination{
        text-align: center;
    }


    .m-text-thin {
        font-weight: 300 !important;
    }

    .m-text-spaced {
        letter-spacing: 1px !important;
    }

    .m-text-lined {
        line-height: 1.8;
    }
    /*透明度*/
    .m-opacity-mini{
        opacity: 0.8 !important;
    }


    /*卡片CSS*/
    .text {
        font-size: 14px;
    }

    .item {
        margin-bottom: 18px;
    }

    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }
    .clearfix:after {
        clear: both
    }
    /*分类导航CSS*/
    .time {
        font-size: 13px;
        color: #999;
    }

    .bottom {
        margin-top: 13px;
        line-height: 12px;
    }

    .button {
        padding: 0;
        float: right;
    }

    .image {
        width: 100%;
        display: block;
    }

    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }

    .clearfix:after {
        clear: both
    }

</style>