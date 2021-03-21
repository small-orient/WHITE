<template>
    <div class="detail-content">

        <el-backtop target=".detail-content"></el-backtop>
        <!--头部导航栏-->
        <div style="margin: 20px 3%;" class="card">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">
                    <i style="color: rgb(234,236,241)">首页</i>
                </el-breadcrumb-item>
                <el-breadcrumb-item :to="{ path: '/ftype' }">
                    <i style="color: rgb(234,236,241)">文章</i></el-breadcrumb-item>
                <el-breadcrumb-item >
                    <i style="color: rgb(234,236,241)">{{blog.title}}</i></el-breadcrumb-item>
            </el-breadcrumb>
        </div>
            <!--内容区-->
            <el-card class="blog-detail animated fadeInUp">
            <div style="text-align: center">
                <h2>{{blog.title}}</h2>
                <span v-if="blog.createSign === '原创'">
                    <el-button type="success"
                               size="mini"
                               circle
                               plain
                    >{{blog.createSign}}</el-button>
                </span>&nbsp;&nbsp;
                <span v-else-if="blog.createSign === '转载'">
                    <el-button type="warning"
                               size="mini"
                               circle
                               plain
                    >{{blog.createSign}}</el-button>
                </span>&nbsp;&nbsp;
                <span v-else>
                    <el-button type="primary"
                               size="mini"
                               circle
                               plain
                    >{{blog.createSign}}</el-button>
                </span>&nbsp;&nbsp;
                <span class="el-icon-time" style="font-size: 12px"
                >&nbsp;{{blog.createTime}}</span>&nbsp;
                <el-link style="font-size: 12px" class="el-icon-folder-opened">
                    &nbsp;{{blog.typeName}}</el-link>&nbsp;
                <span style="font-size: 12px" class="el-icon-view"
                >浏览：{{blog.clickHit}}</span>
            </div>
            <div class="divider-content" ><el-divider ></el-divider></div>
            <!--主体内容-->
            <div class=" blog-content">
                <div class=" typo typo-selection  markdown-body "
                     v-html="blog.content"></div>
            </div>
            <!--文章末尾公共区域-->
            <div>
                <!--赞赏分享-->
                <div style="text-align: center">
                    <div style="padding:20px 0">如果您喜欢这篇文章的话，不妨</div>
                    <el-button type="danger"
                               icon="iconfont icon-dianzan"
                               round
                               v-show="like"
                               @click="addLike"
                    >点赞({{this.likes}})</el-button>
                    <el-button type="danger"
                               icon="iconfont icon-dianzan"
                               round
                               v-show="notLike"
                               @click="delLike"
                    >取消( {{this.likes}} )</el-button>
                    <span style="padding:0 5px" v-show="appreciationState">or</span>
                    <el-popover
                            placement="top-start"
                            title="-支付宝或微信,一分也是爱"
                            width="100px"
                            trigger="hover"
                            v-show="appreciationState"
                    >
                        <el-image style="width: 200px; height: 200px"
                                  :src="PaymentUrl"
                                  :fit="fit"></el-image>
                    <el-button type="warning"
                               icon="iconfont icon-tang"
                               round
                               slot="reference"
                    ><span style="font-size: 14px">打赏&nbsp;&nbsp;&nbsp;</span></el-button>
                    </el-popover>
                    <div style="padding:20px 0">---&nbsp;感谢您的支持！---</div>
                </div>
                <!--版权-->
                <div style="padding:10px 0">
                <el-card style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0);
                background-color: rgba(189,186,186,0.09);" v-show="shareStatementState">
                    <div style="font-size: 15px;color: rgba(7,7,7,0.60)">版权声明
                        <i class="el-icon-warning-outline"></i>：本文为博主
                        <el-link type="primary" style="font-weight: bold">
                            {{blog.nickName}}</el-link> 原创文章，转载请附上原文出处链接。
                    </div>
                    <div  style="font-size: 15px;color: rgba(7,7,7,0.60)">原文链接：{{getUrl()}}</div>
                    <div  style="font-size: 15px;color: rgba(7,7,7,0.60);text-align: right"><el-link
                            type="primary"
                            @click="goAbout"
                            class="el-icon-message">联系作者</el-link>
                    </div>

                </el-card>
                </div>
                <!--留言区-->
                <div style="background-color: rgba(249,252,252,0.9);">
                    <div style="padding:10px 0">
                        <!--通过svg引入显示彩色-->
                        <svg class="iconfont" style="font-size: 40px" aria-hidden="true">
                            <use xlink:href="#icon-Z"></use>
                        </svg>
                        <i>来说两句吧</i>
                    </div>
                    <template>
                        <!--评论-->
                        <div v-show="commentOpenState">
                            <div  class="my-reply" v-clickoutside>
                              <!--  <div  @click="inputFocus"  class="my-reply">-->
                                <el-avatar class="header-img"
                                           :size="40"
                                           icon="el-icon-user-solid"
                                           :src="myHeader"></el-avatar>
                                <div class="reply-info" >
                                    <!--<div
                                            tabindex="0"
                                            contenteditable="true"
                                            id="replyInput"
                                            spellcheck="false"
                                            placeholder="输入评论..."
                                            class="reply-input"
                                            @focus="showReplyBtn" *
                                            @input="onDivInput($event)"
                                    >-->
                                    <el-input
                                            v-model="replyComment"
                                            id="replyInput"
                                            type="textarea"
                                            placeholder="输入评论..."
                                            class="reply-input"
                                            @focus="onInput"
                                            @keyup.enter.native="sendComment"
                                    >
                                    </el-input>

                                </div>
                                <div class="reply-btn-box" v-show="btnShow">
                                    <el-button class="reply-btn"
                                               size="medium"
                                               @click="sendComment"
                                               type="primary">发表评论</el-button>
                                </div>
                            </div>
                            <div v-for="(item,i) in commentList" :key="i" :id="item.id" class="author-title reply-father">
                                <!--第一评论人-->
                                <el-avatar class="header-img" :size="40" :src="item.user.avatar"></el-avatar>
                                <div class="author-info">
                                    <span class="author-name " v-if="item.userId === 1">
                                        {{item.user.nickName}}
                                        <el-button type="primary"
                                                   size="mini"
                                                   circle
                                                   plain>博主</el-button>
                                    </span>
                                    <span class="author-name" v-else>{{item.user.nickName}}</span>
                                    <span class="author-time">{{item.createTime}}</span>
                                </div>
                                <div class="talk-box">
                                    <p>
                                        <span class="reply">{{item.content}}</span>
                                    </p>
                                </div>
                                <div class="icon-btn">
                                  <span >
                                      <el-link
                                              @click="showReplyInput(item.user.nickName,item.id)"
                                              :underline="false"
                                  >回复</el-link>
                                  </span>
                                </div>

                                <!--回复人-->
                                <div class="reply-box">
                                    <div v-for="(reply,j) in item.replyComments" :id="reply.id" :key="j" class="author-title">
                                        <el-avatar class="header-img" :size="40" :src="reply.user.avatar"></el-avatar>
                                        <div class="author-info"  >
                                            <span class="author-name" v-if="reply.userId === 1">
                                                {{reply.user.nickName}}
                                                <el-button type="primary"
                                                           size="mini"
                                                           circle
                                                           plain>博主</el-button>
                                            </span>
                                            <span class="author-name" v-else>{{reply.user.nickName}}</span>
                                            <span class="author-time">{{reply.createTime}}</span>

                                        </div>
                                        <div class="talk-box">
                                            <p>
                                              <!--  <span>回复 {{reply.to}}:</span>-->
                                                <span
                                                      >
                                                    回复 @{{reply.parentName}}:</span>
                                                <span class="reply">{{reply.content}}</span>
                                            </p>
                                        </div>
                                        <div class="icon-btn">
                                            <span >
                                                <el-link
                                                        @click="showReplyInput2(reply.user.nickName,
                                                        item.id,
                                                        reply.id,
                                                        reply.parentCommentId)"
                                                        :underline="false"
                                            >回复</el-link>
                                                 <!-- <el-link
                                                          @click="showReplyInput(i,reply.user.nickName,reply.id)"
                                                  >回复</el-link>-->
                                            </span>
                                        </div>
                                        <div class="reply-box">

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--回复框-->
                            <div  class="my-reply my-comment-reply" id="box" v-show="replyBox" >
                                <!--<div  v-show="_inputShow(i)" class="my-reply my-comment-reply">-->
                                <el-avatar class="header-img" :size="40" :src="myHeader"></el-avatar>
                                <div class="reply-info" >
                                    <el-input
                                            v-model="replyData"
                                            :placeholder="to"
                                            type="textarea"
                                            @keyup.enter.native="sendCommentReply"
                                            class="reply-input reply-comment-input"
                                    >
                                    </el-input>
                                    <!-- <el-input
                                             v-model="replyComment"
                                             :placeholder="to"
                                             class="reply-input reply-comment-input"
                                             @focus="showReplyBtn"*
                                     >
                                     </el-input>-->
                                </div>
                                <div class=" reply-btn-box">
                                    <el-button class="reply-btn"
                                               size="medium"
                                               @click="sendCommentReply"
                                               type="primary">回复评论</el-button>

                                    <el-link  :underline="false">
                                        <span @click="closeReplyInput()" class="el-icon-arrow-up">收起</span>
                                    </el-link>

                                    <!--  <el-button class="reply-btn"
                                                 size="medium"
                                                 @click="sendCommentReply(i)"*
                                                 type="primary">回复评论</el-button>-->
                                </div>
                            </div>
                        </div>
                    </template>

                </div>

            </div>
        </el-card>

    </div>
</template>
<script>
    //渲染Markdown样式的组件
    import "github-markdown-css/github-markdown.css"
    import "../../assets/css/typo.css"
    //导入付款码
    import Payment from "../../assets/img/Payment.png";
    import 'highlight.js/styles/monokai-sublime.css';
    // 引入个性化的vs2015样式
   /* import 'highlight.js/styles/vs2015.css'*/

    const clickoutside = {
        // 初始化指令
        bind(el, binding, vnode) {
            function documentHandler(e) {
                // 这里判断点击的元素是否是本身，是本身，则返回
                if (el.contains(e.target)) {
                    return false;
                }
                // 判断指令中是否绑定了函数
                if (binding.expression) {
                    // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
                    binding.value(e);
                }
            }
            // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
            el.vueClickOutside = documentHandler;
            document.addEventListener('click', documentHandler);
        },
        update() {},
        unbind(el, binding) {
            // 解除事件监听
            document.removeEventListener('click', el.vueClickOutside);
            delete el.vueClickOutside;
        },
    };

    export default {
        name: "FBlogDetail.vue",
        data(){
            return{
                blog:{
                    id:'',
                    title:'title',
                    content:'content1116666666666666611111111111111111111111111111111111111111111111111',
                    typeName:'java',
                    createTime:'2020-20-20',
                    createSign:'',
                    clickHit:'',
                    commentHit:'',
                    commentOpen:0,
                    appreciation:0,
                    shareStatement:0,
                    nickName:'baidetu',
                    likes:0,
                    keyword:[],
                },
                //页面中需显示的状态栏
                commentOpenState:true, //评论栏
                appreciationState:true, //打赏栏
                shareStatementState:true, //版权转载栏
                PaymentUrl:Payment, //引入付款码
                fit:'fill',
                like:true,//点赞按钮显示
                notLike:false,//取消点赞按钮显示
                likes:0,//点赞数回显字段

                //留言区：
                //储存评论数据
                comment:{
                    userId:0,
                    content:'',
                    blogId:0,
                    parentCommentId:-1,
                },

                btnShow: false, //发表评论按钮
                commentId:0, //评论id
                replyData:'',
                replyComment:'',
                myHeader:'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png',
                to:'',
                commentList:[], //显示评论集合
                replyBox:false, //回复框是否显示

            }
        },
        directives: {clickoutside},
        methods:{
            //获取页面URL
            getUrl(){
                return window.location.href
            },
            //显示发表评论按钮
            onInput(){
                this.btnShow = true;
                this.closeReplyInput();
            },
            //跳转至关于我
            goAbout(){
                this.$router.push("/about");
            },
            //发表评论
            sendComment(){
                if(!this.replyComment){
                    this.$message({
                        showClose: true,
                        type:'warning',
                        message:'评论不能为空'
                    })
                }else{
                    //提交评论信息
                    let g_this = this;
                        let token = localStorage.getItem("token");
                        if (token){
                            g_this.comment.userId = g_this.$store.getters.getUser.id;
                            g_this.comment.content = g_this.replyComment;
                            g_this.comment.blogId = g_this.$route.params.blogId
                            g_this.comment.parentCommentId=-1;
                            //储存
                            g_this.axios.post("/comment/save",g_this.comment,{
                                headers:{
                                    "Authorization":localStorage.getItem("token")
                                }
                            }).then(res=>{
                                g_this.$message.success('发表成功');
                                g_this.replyComment = ''
                                g_this.commentData();
                                g_this.btnShow = false;

                            })

                        }else {
                            this.$message({
                                showClose: true,
                                type:'warning',
                                message:'您需要登陆才能评论'
                            })
                        }
                }
            },
            //顶级区域回复：显示回复框
            showReplyInput(nickName,commentId){
                //储存该回复对应的用户id
                this.commentId = commentId;
                //回复框初始信息
                this.to = '@'+nickName+':';
                //根据动态id显示回复框
                this.replyBox = true;
                let replyId = commentId
                let box = document.getElementById('box');
                document.getElementById(replyId).appendChild(box);

            },
            //子代区域回复：显示回复框
            showReplyInput2(nickName,commentParentId,commentId){
                //储存该回复对应的用户id
                this.commentId = commentId;
                //回复框初始信息
                this.to = '@'+nickName+':';
                //根据动态id显示回复框
                this.replyBox = true;
                let replyId = commentId
                let box = document.getElementById('box');
                document.getElementById(replyId).appendChild(box);

            },
            //收起回复框
            closeReplyInput(){
                this.replyBox = false;
            },
            //回复评论
            sendCommentReply(){
                if(!this.replyData){
                    this.$message({
                        showClose: true,
                        type:'warning',
                        message:'评论不能为空'
                    })
                }else {
                    let g_this = this;
                    let token = localStorage.getItem("token");
                    if (token) {
                        g_this.comment.userId = g_this.$store.getters.getUser.id;
                        g_this.comment.content = g_this.replyData;
                        g_this.comment.blogId = g_this.$route.params.blogId
                        g_this.comment.parentCommentId = g_this.commentId;
                        //储存
                        g_this.axios.post("/comment/save", g_this.comment, {
                            headers: {
                                "Authorization": localStorage.getItem("token")
                            }
                        }).then(res => {

                            g_this.$message.success('回复成功');
                            g_this.replyData = ''
                            g_this.commentData();
                            this.replyBox = false;
                        })
                    }else {
                        this.$message({
                            showClose: true,
                            type:'warning',
                            message:'您需要登陆才能评论'
                        })
                    }
                }
            },
            //初始化评论信息
            commentData(){
                const blogId = this.$route.params.blogId
                let g_this = this;
                /*评论内容回显*/
                g_this.axios.get('/comment/comments/'+blogId).then(res =>{
                    /*console.log(res+"博客评论信息回显------------------")*/
                    g_this.commentList = res.data.data;
                   /* g_this.myHeader = 'https://localhost:8081'+g_this.$store.getters.getUser.avatar*/
                    g_this.myHeader = 'https://www.blog.baidetu.cn'+g_this.$store.getters.getUser.avatar
                })
            },
            //博客点赞+1
            addLike(){
                const blogId = this.$route.params.blogId
                let g_this = this;
                g_this.axios.get('/blog/addLike/'+blogId).then(res =>{
                  /*  this.$message.success("感谢支持！")*/
                    //刷新点赞按钮
                    g_this.getLikes();
                    //隐藏点赞按钮,显示取消点赞按钮
                    g_this.like = false;
                    g_this.notLike = true;
                })
            },
            //博客点赞-1
            delLike(){
                const blogId = this.$route.params.blogId
                let g_this = this;
                g_this.axios.get('/blog/removeLike/'+blogId).then(res =>{
                    /*this.$message.success("取消成功")*/
                     //刷新点赞按钮
                    g_this.getLikes();
                    //显示点赞按钮,隐藏取消点赞按钮
                    g_this.like = true;
                    g_this.notLike = false;
                })
            },
            getLikes(){
                const blogId = this.$route.params.blogId
                let g_this = this;
                g_this.axios.get('/blog/detail/likes/'+blogId).then(res =>{
                    g_this.likes = res.data.data;
                })
            }
    },
        created() {
            /*博客内容*/
            const blogId = this.$route.params.blogId
            const g_this = this;
            this.axios.get('/blog/detail/'+blogId).then(res =>{
                const blog = res.data.data;
                g_this.blog.id =blog.id;
                g_this.blog.title =blog.title;
                g_this.blog.typeName =blog.typeName;
                g_this.blog.createTime =blog.createTime;
                g_this.blog.createSign =blog.createSign;
                g_this.blog.commentHit =blog.commentHit;
                g_this.blog.clickHit =blog.clickHit;
                g_this.blog.keyword =blog.keyword;
                g_this.blog.likes =blog.likes;
                g_this.blog.appreciation =blog.appreciation;
                g_this.blog.shareStatement =blog.shareStatement;
                g_this.blog.commentOpen =blog.commentOpen;

                g_this.appreciationState = g_this.blog.appreciation === 0;
                g_this.shareStatementState = g_this.blog.shareStatement === 0;
                g_this.commentOpenState = g_this.blog.commentOpen === 0;

                //渲染MD文本
                //代码高亮
                const hljs = require('highlight.js');
                const Markdown = require("markdown-it")({
                    highlight: function (str, lang) {
                        if (lang && hljs.getLanguage(lang)) {
                            try {
                                return '<pre class="hljs"><code>' +
                                    hljs.highlight(lang, str, true).value +
                                    '</code></pre>';
                            } catch (__) {}
                        }

                        return '<pre class="hljs"><code>' + md.utils.escapeHtml(str) + '</code></pre>';
                    }
                });
                /*const md = new Markdown();*/
                g_this.blog.content = Markdown.render(blog.content) ;
            })

            //同时更新该篇博客点击数+1
            this.axios.get('/frontBlog/addClick/'+blogId).then(res =>{
                const blog = res.data.data;
                g_this.blog.clickHit =blog.clickHit;
                    });

            //获取评论
            g_this.commentData();

            //获取点赞数
            g_this.getLikes();

          /*  //获取父组件页面滚动值，赋值
            this.$nextTick(() => {
                this.pageTop = this.scrollTop
            })*/
        },
        watch:{
           /* scrollTop (val) {
                this.onScroll()
                this.pageTop = val
            }*/
        }
    }
</script>

<style scoped>


    .markdown-body {
        box-sizing: border-box;
        min-width: 200px;
        max-width: 980px;
        margin: 0 auto;
        padding: 45px;
    }

    @media screen and (max-width: 992px){
        .detail-content{
            max-width: 960px;
            margin: 1px auto;
        }
        .divider-content{
            max-width: 98%;
            margin: 1px auto;
        }
        .blog-detail{
            box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
            min-height: 700px;
            padding:20px 15px;
        }

    }

    @media screen and (min-width: 992px){
        .detail-content{
            max-width: 1080px;
            margin: 1px auto;
        }
        .blog-detail{
            box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
            max-width: 80%;
            margin-left: 9%;
            min-height: 700px;
            padding:20px 15px;
        }
        .divider-content{
            max-width:98%;
            margin: 1px auto;
        }

    }

    .blog-detail{
        background-color: rgba(255, 255, 255,0.90);
    }

    .blog-content{
        padding:7px 1px;
    }


    /*留言区CSS*/
    .my-reply{
        padding :10px;
        background-color :#fafbfc
    }
    .header-img{
    display :inline-block;
    vertical-align :top
    }
    .reply-info{
        display :inline-block;
        margin-left :5px;
        width :90%;
    }
    .reply-input{
        min-height: 20px;
        line-height: 22px;
        padding :10px 10px  ;
        color :#ccc;
        background-color: #fff;
        border-radius :5px;

    }

    .reply-btn-box{
        height :25px;
        margin :10px 0;
    }
    .reply-btn-box{
        position:relative;
        float :right;
        margin-right: 15px;
    }
    .my-comment-reply{
        margin-left :50px
    }
    .author-title:not(:last-child){
        border-bottom: 1px solid rgba(178,186,194,.3);
    }
    .author-title{
        padding :10px
    }
    .header-img{
        display: inline-block;
        vertical-align :top;
    }
    .author-info{
        display :inline-block;
        margin-left :5px;
        width :60%;
        height :40px;
        line-height :20px;
    }
    .author-info>span{
        display: block;
        cursor :pointer;
        overflow: hidden;
        white-space: nowrap;
        text-overflow :ellipsis;
    }
    .author-name{
        color : #424242;
        font-size :16px;
        font-weight :bold;
    }
    .author-time{
        font-size: 14px
    }
    .icon-btn {
        width :30%;
        padding-left: 90%;
    }
    .icon-btn >span{
        cursor: pointer
    }
    .iconfont{
        margin :0 5px
    }
    .talk-box {
        margin :0 50px
    }
    .talk-box > p{
        margin :0
    }
    .reply{
        font-size: 16px;
        color :#424242;
    }
    .reply-box{
            margin: 10px 0 0 50px;
          background-color : #fff
    }
</style>