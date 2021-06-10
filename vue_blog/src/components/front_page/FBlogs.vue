<template>
    <div class="blogs-content">
        <!--头部导航栏-->
        <div style="margin: 20px 3%;" class="card">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">
                    <i style="color: rgb(234,236,241)">首页</i>
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <!--公告-->
        <el-card style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        background-color:rgba(0, 0, 0, 0.1);margin: 0 8%"
                 class="blog-card animated fadeInLeft">
            <span style="color: #f5f8f8">
                <i class="iconfont icon-laba" style="font-size: 25px;"></i>
                : 网站已开源至GitHub，感兴趣的朋友可以了解~~</span>
        </el-card>
        <el-row :gutter="10" >
            <el-col  :xs="24" :sm="24" :md="16" :lg="16" :xl="16">
                <div class="grid-content m-frame">
                        <!--左边栏外框-->
                        <div class="block" >
                            <el-timeline class="card-blogs" style="min-height: 800px">
                                    <el-timeline-item
                                            :timestamp="blog.createTime"
                                            placement="top"
                                            v-for="blog in blogs">
                                        <el-card
                                                style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)"
                                                class="blog-card animated fadeInLeft">
                                                <el-row :gutter="20">
                                                    <el-col :span="4">
                                                        <div class="grid-content ">
                                                            <el-image
                                                                    :src="$store.getters.getAddress+blog.blogImg"
                                                                    style="max-width: 100px; max-height: 100px;min-width: 50px;min-height: 50px"
                                                                    fit="fill"
                                                            >
                                                            </el-image>
                                                        </div>
                                                    </el-col>
                                                    <el-col :span="20">
                                                        <!--博客数据-->
                                                        <div class="blog-title" >
                                                            <h3>
                                                                <router-link :to="{name:'FBlogDetail',params:{blogId:blog.id}}">
                                                                    <span style="color: rgb(58,62,74)">{{blog.title}}</span>
                                                                </router-link>
                                                            </h3>
                                                            <span>{{blog.summary.substring(0,23)+"..."}}</span>
                                                        </div>
                                                    </el-col>
                                                    <!--博客提示信息-->
                                                    <div class="blog-info" >
                                                        <div style="margin-top: 12px">
                                                            <span class="el-icon-s-custom word-color" >&nbsp;{{blog.nickName}}</span>&nbsp;
                                                            <span class="el-icon-document-checked word-color">&nbsp;{{blog.typeName}}</span>&nbsp;
                                                            <span class="el-icon-chat-line-round word-color" >&nbsp;{{blog.commentHit}}</span>&nbsp;
                                                            <span class="el-icon-view el-icon--right word-color">&nbsp;{{blog.clickHit}}</span>
                                                        </div>
                                                    </div>
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
                                @current-change=f_page
                        ></el-pagination>
                    </div>
                </div>

            </el-col>
            <el-col  :xs="24" :sm="24" :md="8" :lg="8" :xl="8" style="margin-top: 22px">
                <div class=" animated fadeInRight">
                <div class=" m-frame-right">
                    <div style="margin-top: 30px">
                        <!--右边栏卡片-->
                        <el-card  class="card-right widescreen" style="text-align: center">
                            <!--自我介绍-->
                            <div class="avatar-blogs">
                                <el-avatar :size="avatarState"
                                           :src="blogger.avatar">
                                </el-avatar>
                            </div>
                            <div style="text-align: center;padding-top: 20px">
                                <span class="iconfont"
                                      style="font-weight:bold;font-size: 30px"
                                >{{blogger.nickName}}</span>
                            </div>
                            <div style="text-align: center;padding-top: 10px;">
                                <span  style="font-size: 18px">
                                    小站会长期、不定时更新~
                                </span>
                            </div>
                           <el-divider></el-divider>
                            <!--扫码部分-->
                            <div class="scan-code">
                                <el-popover
                                        placement="top-start"
                                        width="100px"
                                        trigger="hover">
                                    <el-image style="width: 100px; height: 100px"
                                              :src="qqUrl"
                                              :fit="fit"></el-image>
                                    <span  slot="reference">
                                    <i class="iconfont icon-QQ"
                                       style="font-size: 30px;"></i></span>
                                </el-popover>
                                <el-popover
                                        placement="top-start"
                                        width="100px"
                                        trigger="hover">
                                    <el-image style="width: 100px; height: 100px"
                                              :src="weixinUrl"
                                              :fit="fit"></el-image>
                                    <span slot="reference">
                                    <i class="iconfont icon-weixin3"
                                       style="font-size: 30px;padding-left:10px "></i>
                                     </span>
                                </el-popover>
                                    <el-tooltip content="baidetu2020@163.com" placement="top" style="padding-bottom: 6px">
                                        <el-link
                                                type="info"
                                                class="iconfont icon-youxiang"
                                                style="font-size: 30px;padding-left:10px"
                                                :underline="false"
                                        ></el-link>
                                    </el-tooltip>
                                <el-tooltip content="跳转GitHub" placement="top" style="padding-bottom: 6px">
                                    <el-link
                                            type="info"
                                            class="iconfont icon-github"
                                            style="font-size: 30px;padding-left:10px"
                                            :underline="false"
                                            href="https://github.com/small-orient"
                                            target="_blank"
                                    ></el-link>
                                </el-tooltip>
                                <el-tooltip content="去留言~" placement="top" style="padding-bottom: 6px">
                                    <el-link
                                            type="info"
                                            class="iconfont icon-liuyan4"
                                            style="font-size: 30px;padding-left:10px"
                                            :underline="false"
                                            @click="toWall()"
                                    ></el-link>
                                </el-tooltip>
                            </div>
                        </el-card>
                    </div>
                </div>

                <div class=" m-frame-right">
                <!--右边栏卡片-->
                <el-card class="card-right widescreen"  >
                    <div slot="header" class="clearfix">
                        <!--通过svg引入显示彩色-->
                        <svg class="iconfont" aria-hidden="true">
                            <use xlink:href="#icon-huore1"></use>
                        </svg>
                        <span style="color: cornflowerblue">&nbsp;热文排行</span>
                    </div>
                    <div v-for="(hotblog,index) in hotBlogs" :key="index" v-if="index < 9" class="text item">
                        <div >

                            <el-button type="danger" v-if="index < 2">{{index+1}}</el-button>
                            <el-button type="warning" v-else-if="index < 6">{{index+1}}</el-button>
                            <el-button type="primary" v-else>{{index+1}}</el-button>
                            <router-link  :to="{name:'FBlogDetail',params:{blogId:hotblog.id}}">
                                <span class="title-blogs">
                                    <i>&nbsp;&nbsp;</i>
                                     <span style="color: rgb(86,132,238)">{{hotblog.title.substring(0,15)}}</span>
                                </span>
                             </router-link>

                              <span  class="clickHit-blogs" style="margin-left: 3%;color: #f38006">{{hotblog.clickHit}}阅</span>
                        </div>

                    </div>
                </el-card>
                </div>

                <div class=" m-frame-right">
                    <div class="m_f_flag">
                        <!--标签-->
                        <el-card class="card-right widescreen"  >
                            <div slot="header" class="clearfix">
                                <!--通过svg引入显示彩色-->
                                <span class="el-icon-position"
                                        style="color: cornflowerblue">&nbsp;标签</span>
                            </div>
                            <span v-for="(sign,index) in blogKeywords"
                                  :key="index" class="text item">
                                  <el-link style="padding:10px;" @click="f_keywordSearch(sign.id)">
                                        <span class="sign-blogs" style="color: rgba(64,158,255,1)" >{{sign.keyword}}</span>
                                </el-link>
                            </span>
                        </el-card>
                    </div>
                </div>
                <div class="  m-frame-right">
                    <div class="m_f_flag">
                        <!--最近发布-->
                        <el-card class="card-right widescreen" >
                            <div slot="header" class="clearfix">
                                <!--通过svg引入显示彩色-->
                                <span
                                        class="el-icon-aim"
                                        style="color: cornflowerblue">&nbsp;最近动态</span>
                            </div>
                            <div v-for="(blog,index) in blogs"
                                 :key="index"
                                 v-if="index < 9"
                                 class="text item">
                                <span>
                                    <el-button type="primary">{{index+1}}</el-button>
                                    <router-link :to="{name:'FBlogDetail',params:{blogId:blog.id}}">
                                        <i>&nbsp;&nbsp;</i>
                                        <span class="title-blogs">
                                           <span style="color: rgb(86,132,238)">
                                               {{blog.title.substring(0,15)}}
                                           </span>
                                        </span>
                                    </router-link>
                                    <span class="clickHit-blogs"
                                          style="margin-left:  3%;color: #f38006">{{blog.clickHit}}阅</span>
                                </span>

                            </div>
                        </el-card>
                    </div>
                </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import qq from "../../assets/img/1329440214QQ.jpg";
    import weixin from "../../assets/img/weixin.png";

    export default {
        name: "Fblogs",
        data() {
            return {
                blogs: {},//博客数据
                query: {
                    //分页信息
                    currentPage: 1,
                    pageSize: 13,
                    pageTotal: 0
                },
                hotBlogs:{}, //热文博客集合
                blogKeywords:[], //博客标签数组
                //控制显示博客封面图片
                imgDisplay:true,
                qqUrl: qq,
                weixinUrl: weixin,
                fit:'fill',
                avatarState:130,//博主头像大小
                blogger:{
                    avatar:'',//博主头像
                    nickName:'',//博主昵称
                },
                timer: false,//浏览器定时器
                screenWidth: document.body.clientWidth,//监听浏览器宽度
                scrollTop: 0,

            }
        },
        methods:{
            //分页
            f_page(currentPage,typeName_t,title_t) {
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
                   /* console.log(res+"分页");*/
                    g_this.blogs = res.data.data.records;
                    g_this.query.currentPage = res.data.data.current;
                    g_this.query.pageTotal = res.data.data.total;
                    g_this.query.pageSize = res.data.data.size;
                });
            } ,

            //根据标签关键字查询博客
            f_keywordSearch(keywordId){
                const g_this = this;
                this.axios.get("/keyword/getBlogs/"+keywordId).then(res => {
                    //重新设置分页信息
                    g_this.blogs = res.data.data.records;
                    g_this.query.currentPage = res.data.data.current;
                    g_this.query.pageTotal = res.data.data.total;
                    g_this.query.pageSize = res.data.data.size;
                });


            },
            //去留言板
            toWall(){
                this.$router.push("/fwall");
            }
        },
        mounted() {
            const _this = this;
            // 监听浏览器窗口变化
            window.onresize = function() {
                // console.log("agag")
                return (() => {
                    window.screenWidth = document.body.clientWidth;
                    _this.screenWidth = window.screenWidth;
                })();
            };
            //改变右边头像大小
            if (_this.screenWidth < 992){
                _this.avatarState = 200
            }

        },
        created() {
            //开始渲染页面时直接调用分页方法渲染数据
            this.f_page(1,null,null);

            const g_this = this;
            //获取热文博客
            this.axios.get("/blog/hot").then(res =>{
               /* console.log(res+"热文博客数据+++++++++")*/
                g_this.hotBlogs = res.data.data;
            })

/*            this.axios.get("http://localhost:8081/blog/hot").then(res =>{
                g_this.hotBlogs = res.data.data;
            })*/

            //获取标签
            this.axios.get("/keyword/get").then(res =>{
                g_this.blogKeywords = res.data.data;
            })

            //获取博主头像
            this.axios.get("/user/bloggerAvatar").then(res =>{
                g_this.blogger= res.data.data;
            })


        },
        watch: {
            //监听浏览器宽度
            screenWidth: {
                immediate: true,
                handler: function(newVal) {
                    // 为了避免频繁触发resize函数导致页面卡顿，使用定时器
                    if (!this.timer) {
                        this.screenWidth = newVal;
                        this.timer = true;
                        let _this = this;
                        setTimeout(() => {
                            //在这里做有关浏览器变化时需要做的操作
                            _this.timer = false;
                        }, 400);
                    }
                }
            }
        },
        destroyed () {
        },

    }
</script>

<style scoped>


    /*屏幕小于992px则显示大小为960px,  card属性距离边框左右7%*/

    @media screen and (max-width: 992px){
        .blogs-content{
            max-width: 960px;
            margin: 1px auto;
        }
        .card{
            margin:0 9%;
            border-radius: 10px;
        }
        .blog-card{
            border-radius: 5px;
            background-color: rgba(255, 255, 255, 0.9);
        }
        .avatar-blogs{
            padding-top: 10px;
            padding-bottom: 10px;
        }
        .card-right{
            margin:0 12%;
            border-radius: 8px;
            background-color: rgba(255, 255, 255, 0.9);
        }
        /*右边边框距离调整*/
        .m-frame-right{
            margin: 30px auto;
            padding-left: 2%;
            padding-right: 1%;
            display: none;
        }
        .card-blogs{
            margin:20px 9%;
            border-radius: 10px;
        }
        .title-blogs{
            font-size: 20px;
            color: rgb(86,132,238)
        }
        .clickHit-blogs{
            font-size: 15px;
            color: #3b3f4b;
        }
        .sign-blogs{
            font-size: 20px;
            color: #3b3f4b;
        }
        .blog-title{
            margin-left: 13%
        }
        .blog-info{
            margin-left: 29%;
            margin-top: 12%;
        }

    }


    @media screen and (min-width: 992px){
        .blogs-content{
            max-width: 1080px;
            margin: 1px auto;
        }
        .card{
            border-radius: 10px;
        }
        .widescreen{
            width: 90%;
        }
        .blog-card{
            border-radius: 5px;
            background-color: rgba(255, 255, 255, 0.9);
        }
        .avatar-blogs{
            padding-bottom: 10px;
            padding-top: 10px
        }
        /*中间栏边框距离调整*/
        .m-frame{
            margin: 30px auto;
        }

        .m-frame-right{
            margin: 30px auto;
        }
        .card-blogs{
            margin:0 5%;
            border-radius: 10px;
        }
        .card-right{
            background-color: rgba(255, 255, 255, 0.9);
        }
        .blog-title{
            margin-left: 5%
        }
        .blog-info{
            margin-left: 22%;
            margin-top: 12%;
        }


    }


    .text {
        font-size: 14px;
    }

    .item {
        padding:10px 10px
    }

    .clearfix:after {
        display: table;
        content: "";
    }
    .clearfix:after {
        clear: both
    }
    .word-color{
        color: rgba(11,11,11,0.60)
    }


</style>