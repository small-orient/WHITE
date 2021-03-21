<template>
    <div>
        <!--头部导航栏-->
        <div style="margin: 20px 3%">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item class="el-icon-user-solid" style="color: white" :to="{ path: '/user' }">
                    <i style="color: rgb(234,236,241)">个人中心</i></el-breadcrumb-item>
                <el-breadcrumb-item ><i style="color: rgb(234,236,241)">评论查看</i></el-breadcrumb-item>
            </el-breadcrumb>
        </div>
            <el-tabs v-model="activeName" @tab-click="handleClick">
                <!--用户个人发表的评论-->
                    <el-card class="user-comment-card card animated pulse "
                             style="min-height: 800px;">
                        <div style="padding-top: 33px"></div>
                    <div v-for="(item,i) in commentList" :key="i" :id="item.id" class="author-title reply-father">
                        <!--第一评论人-->
                        <el-avatar class="header-img" :size="40" :src="item.user.avatar"></el-avatar>
                        <div class="author-info">
                            <span class="author-name" >我
                            (在博客 <el-link type="primary" style="padding-bottom: 6px"
                                          @click="toBlogDetail(item.blogId)"
                                >
                                            <span style="font-size: 16px">
                                            {{item.title}}
                                            </span>
                                        <i class="el-icon-chat-dot-round"></i>
                                        </el-link> 中评论)
                            </span>

                            <span class="author-time">{{item.createTime}}</span>
                        </div>
                        <div class="talk-box">
                            <p>
                                <span class="reply">{{item.content}}</span>
                            </p>
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
                                        <span >回复 我:</span>
                                        <span class="reply">{{reply.content}}</span>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    </el-card>
            </el-tabs>
    </div>
</template>

<script>
    export default {
        name: "FUserComment",
        data(){
            return{
                activeName: 'first', //标签头默认选择栏
                commentList:[], //显示评论集合
                userId:0,
                myHeader:'https://ae01.alicdn.com/kf/Hd60a3f7c06fd47ae85624badd32ce54dv.jpg',
            }
        },
        methods:{
            //页面首显内容
            firstData(){
                const g_this = this;
                let userId = g_this.$store.getters.getUser.id;
                    g_this.axios.get("/comment/userComment/"+userId).then(res => {
                        console.log(res.data.data+"用户在博客文章评论----------")
                        g_this.commentList = res.data.data;

                        g_this.myHeader = g_this.$store.getters.getUser.avatar
                    });
            },
            //被选中的标签实例
            handleClick(tab, event) {
                const g_this = this;
                /*console.log(tab, event);*/
                /* console.log(tab.paneName, event);*/

                g_this.firstData(tab.paneName);

            },
            //跳转博客详情
            toBlogDetail(blogId){
                this.$router.push("/fblog/"+blogId);
            },


        },
        created() {
            const g_this = this;
            g_this.userId = g_this.$store.getters.getUser.id;
            this.firstData()
        }
    }
</script>

<style scoped>
    @media screen and (max-width: 992px){
        .user-comment-card{
            margin-right: 15%;
            background-color: rgba(141,159,212,0.55);
        }
        .author-title{
            padding :10px;
            margin-left: 30px;
        }
    }


    @media screen and (min-width: 992px){
        .user-comment-card{
            margin-right: 19%;
            background-color: rgba(236, 238, 245, 0.70);
        }
        .author-title{
            padding :10px;
            margin-left: 90px;
        }
    }

    .author-title:not(:last-child){
        border-bottom: 1px solid rgba(178,186,194,0.3);

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