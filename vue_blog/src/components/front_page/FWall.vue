<template>
    <div class="wall-content">
        <!--头部导航栏-->
        <div style="margin: 20px 3%" class="card">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item >留言墙</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <el-card class="box-card card animated pulse " style="background:rgba(255,255,255,0.85);min-height: 800px">
        <el-row>
            <el-col :span="24" >
            <div style="text-align: center">
                <span style="font-size: 35px;">留言墙</span>
            </div>
            <div
                style="text-align: center;
                        margin-top: 20px;
                        margin-bottom: 10px;
                        color: rgba(11,11,11,0.48)" >
                <!--实时时间显示-->
                <span id="Date" class="el-icon-time">
                    &nbsp;{{time.year+'年'+time.mon+'月'+time.da+'日'+time.h+':'+time.m+':'+time.s}}</span>
            </div>
            </el-col>
        </el-row>
        <hr style="color: #c0c4cc;border-style:dotted;margin: 0 10%">
            <template>
                <!--评论-->
                <div>
                    <div  class="my-reply" >
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
                                    :autosize="{ minRows: 5, maxRows: 11}"
                                    placeholder="输入留言..."
                                    class="reply-input "
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
                    <div style="padding-top: 33px"></div>
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
                                  <span style="margin-left: 5px" v-if="user.id === 1">
                                      <el-link
                                              class="el-icon-delete"
                                              type="danger"
                                              @click="deleteComment(item.id)"
                                              :underline="false"
                                      >删除</el-link>
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
                                        <span>回复 @{{reply.parentName}}:</span>
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
                                            </span>
                                            <span
                                                    style="margin-left: 5px"
                                                    v-if="user.id === 1"
                                            >
                                              <el-link
                                                      class="el-icon-delete"
                                                      type="danger"
                                                      @click="deleteComment(reply.id)"
                                                      :underline="false"
                                              >删除</el-link>
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
        </el-card>
    </div>

</template>

<script>
    /**
     * 留言墙
     *
     */
    export default {
        name: "FWall",
        data(){
            return{
                //留言区：
                //储存评论数据
                comment:{
                    userId:0,
                    content:'',
                    blogId:0,
                    parentCommentId:-1,
                },
                //显示时间
                time:{
                    year:0,
                    mon:0,
                    da:0,
                    h:0,
                    m:0,
                    s:0,
                },
                //用户
                user:{
                  id:0
                },
                btnShow: false, //发表评论按钮
                commentId:0, //评论id
                replyData:'',
                replyComment:'',
                myHeader:'https://ae01.alicdn.com/kf/Hd60a3f7c06fd47ae85624badd32ce54dv.jpg',
                to:'',
                commentList:[], //显示评论集合
                replyBox:false, //回复框是否显示
            }
        },
        methods:{
            //显示发表评论按钮
            onInput(){
                this.btnShow = true;
                this.closeReplyInput();
            },
            //发表评论
            sendComment(){
                if(!this.replyComment&&this.replyComment===""){
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
                        g_this.comment.blogId = 0;
                        g_this.comment.parentCommentId=-1;
                        //储存
                        g_this.axios.post("/message/saveLeaveMessage",g_this.comment,{
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
                        g_this.comment.blogId = 0;
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
            //删除评论
            deleteComment(commentId){
                const g_this = this;
                g_this.axios.get("/comment/delete/" + commentId).then(res => {
                        g_this.$message.success('删除成功');
                        g_this.commentData();
                });
            },
            //初始化评论信息
            commentData(){
                let g_this = this;
                /*评论内容回显*/
                g_this.axios.get('/message/leaveMessages/').then(res =>{
                   /* console.log(res+"留言墙评论信息回显------------------")*/
                    g_this.commentList = res.data.data;
                    /*g_this.myHeader = "http://118.178.188.20:9707/"+g_this.$store.getters.getUser.avatar*/
                    g_this.myHeader = "https://www.blog.baidetu.cn/"+g_this.$store.getters.getUser.avatar
                })
            },
        },
        created() {
            const g_this = this;
            //获取评论
            g_this.commentData();
            g_this.user.id = g_this.$store.getters.getUser.id;
            //实时时间显示
            setInterval(function(){
                const date = new Date();
                g_this.time.year=date.getFullYear(); //获取当前年份
                g_this.time.mon=date.getMonth()+1; //获取当前月份
                g_this.time.da=date.getDate(); //获取当前日
                g_this.time.h=date.getHours(); //获取小时
                g_this.time.m=date.getMinutes(); //获取分钟
                g_this.time.s=date.getSeconds(); //获取秒
               /* const d=document.getElementById('Date');
                d.innerHTML=' '+year+'年'+mon+'月'+da+'日'+' '+h+':'+m+':'+s;  },*/},0)
        }
    }
</script>

<style scoped>
    @media screen and (max-width: 992px) {
        .wall-content {
            max-width: 960px;
            margin: 1px auto;

        }
        .card{
            margin:0 7%;

        }
        .reply-info{
            display :inline-block;
            margin-left :16px;
            width :90%;
        }
    }


    @media screen and (min-width: 992px) {
        .wall-content {
            max-width: 1080px;
            margin: 1px auto;
        }
        .card{

            margin:0 7%;
        }
        .reply-info{
            display :inline-block;
            margin-left :30px;
            width :90%;
        }

        }
    /*留言区CSS*/
    .my-reply{
        padding :10px;
    }
    .header-img{
        display :inline-block;
        vertical-align :top
    }

    .reply-input{
        min-height: 20px;
        line-height: 22px;
        padding :10px 10px  ;
        color :#ccc;
        background:rgba(255,255,255,0.7);
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
        border-bottom: 1px solid rgba(178,186,194,0.3);

    }
    .author-title{
        padding :10px;
        margin-left: 30px;
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
        padding-left: 85%;
    }
    .icon-btn >span{
        cursor: pointer
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
        background:rgba(255,255,255,0.3);
    }

</style>