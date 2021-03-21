<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 评论信息
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <!--标签头-->
            <template>
                <el-tabs v-model="activeName" @tab-click="handleClick">
                    <!--文章评论-->
                    <el-tab-pane label="我的文章评论" name="first">
                        <div v-for="(commentList,index) in comments" style="margin-bottom: 20px">
                            <el-card v-if="commentList.length>0">
                                <div slot="header" class="clearfix">
                                        <span v-for="(comment,i) in commentList" v-if="i<1">
                                           <el-link type="primary" style="padding-bottom: 0.8%"
                                                    @click="toBlogDetail(comment.blogId)"
                                           >
                                                <span style="font-size: 16px">
                                                {{comment.title}}
                                                </span>
                                            </el-link>
                                        </span>
                                    <el-button style="float: right;padding: 3px 0"
                                               type="text"
                                               @click="closeCard(index)"
                                               :id="index+10085"
                                    >收起
                                    </el-button>
                                    <el-button style="float: right;padding: 3px 0;display: none"
                                               type="text"
                                               :id="index+10086"
                                               @click="openCard(index)"
                                    >恢复
                                    </el-button>
                                </div>
                                <el-card :id="index">
                                    <div v-for="comment in commentList" :id="comment.id">
                                        <el-avatar class="header-img"
                                                   :size="40"
                                                   :src="comment.user.avatar">
                                        </el-avatar>

                                        <span style="color: rgba(50,50,50,0.9)">
                                         <span
                                                 style="padding-left: 1%;font-weight: bold;"
                                                 v-if="comment.userId === 1"
                                         >我
                                        </span>
                                        <span
                                                style="padding-left: 1%;font-weight: bold;"
                                                v-else
                                        >{{comment.user.nickName}}
                                        </span>
                                        于
                                        <span>{{comment.createTime}}</span>
                                         <span v-if="comment.userId === 1">
                                             回复了文章
                                         </span>
                                        <span v-else>
                                          回复了你的文章
                                         </span>

                                        <el-link type="primary" style="padding-bottom: 6px"
                                                 @click="toBlogDetail(comment.blogId)"
                                        >
                                            <span style="font-size: 16px">
                                            {{comment.title}}
                                            </span>
                                        <i class="el-icon-chat-dot-round"></i>
                                        </el-link>
                                        <i style="padding-left: 500px"></i>
                                        <el-link type="success"
                                                 @click="reply(
                                                     comment.id,comment.blogId)">快速回复</el-link>
                                        <el-divider direction="vertical"></el-divider>
                                            <!--删除评论提示框-->
                                        <template>
                                            <el-popconfirm
                                                    confirm-button-text='确定'
                                                    cancel-button-text='再想想'
                                                    icon="el-icon-info"
                                                    icon-color="red"
                                                    title="评论一旦删除，不可恢复。确定要删除吗?"
                                                    @onConfirm="confirm(comment.id)"
                                            >
                                                <el-button type="text" slot="reference">
                                                    <el-link type="danger">删除</el-link></el-button>
                                            </el-popconfirm>
                                        </template>
                                    </span>

                                        <div style="padding-top: 3% ;
                                color: rgba(50,50,50,0.9);
                                padding-left: 3%">
                                            "{{comment.content}}"
                                        </div>

                                        <div class="divider-content">
                                            <el-divider></el-divider>
                                        </div>
                                    </div>
                                </el-card>
                            </el-card>

                        </div>
                    </el-tab-pane>
                    <div id="replyBox" v-show="replyBox" style="padding-bottom: 10px">
                        <el-input
                                type="textarea"
                                :rows="2"
                                style="max-width: 80%"
                                placeholder="请输入内容"
                                v-model="textarea">
                        </el-input>
                        <el-button slot="append" :size="20"
                                   style="margin-bottom: 5px;margin-left: 20px"
                                   @click="replyComment">
                            回复评论
                        </el-button>
                        <el-link :underline="false">
                            <span @click="closeReplyInput()" class="el-icon-arrow-up">收起</span>
                        </el-link>
                    </div>
                    <!--博主评论-->
                    <el-tab-pane label="我发表的评论" name="second">
                        <div>
                            <el-card v-for="(item,y) in commentList" style="margin-bottom: 20px">
                                <!--第一评论人-->
                                <el-avatar class="header-img" :size="40" :src="item.user.avatar"></el-avatar>
                                <div class="author-info">
                            <span class="author-name">我
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
                                        <i style="padding-left: 500px"></i>
                                        <!--删除评论提示框-->
                                        <template>
                                            <el-popconfirm
                                                    confirm-button-text='确定'
                                                    cancel-button-text='再想想'
                                                    icon="el-icon-info"
                                                    icon-color="red"
                                                    title="评论一旦删除，不可恢复。确定要删除吗?"
                                                    @onConfirm="confirm(item.id)"
                                            >
                                                <el-button type="text" slot="reference">
                                                    <el-link type="danger">删除</el-link></el-button>
                                            </el-popconfirm>
                                        </template>
                                    </p>
                                </div>
                                <!--回复人-->
                                <div class="reply-box">
                                    <div v-for="(replyCom,j) in item.replyComments" :id="replyCom.id" :key="j"
                                         class="author-title">
                                        <el-avatar class="header-img" :size="40" :src="replyCom.user.avatar"></el-avatar>
                                        <div class="author-info">
                                            <span class="author-name" v-if="replyCom.userId === 1">
                                                {{replyCom.user.nickName}}
                                                <el-button type="primary"
                                                           size="mini"
                                                           circle
                                                           plain>博主</el-button>
                                            </span>
                                            <span class="author-name" v-else>{{replyCom.user.nickName}}</span>
                                            <span class="author-time">{{replyCom.createTime}}</span>
                                        </div>
                                        <div class="talk-box">
                                            <p>
                                                <!--  <span>回复 {{reply.to}}:</span>-->
                                                <span>回复 我:</span>
                                                <span class="reply">{{replyCom.content}}</span>
                                                <i style="padding-left: 500px"></i>
                                                <el-link type="success"
                                                         @click="reply2(replyCom.id,item.blogId)">快速回复</el-link>
                                                <el-divider direction="vertical"></el-divider>
                                                <!--删除评论提示框-->
                                                <template>
                                                    <el-popconfirm
                                                            confirm-button-text='确定'
                                                            cancel-button-text='再想想'
                                                            icon="el-icon-info"
                                                            icon-color="red"
                                                            title="评论一旦删除，不可恢复。确定要删除吗?"
                                                            @onConfirm="confirm(replyCom.id)"
                                                    >
                                                        <el-button type="text" slot="reference">
                                                            <el-link type="danger">删除</el-link></el-button>
                                                    </el-popconfirm>
                                                </template>
                                            </p>
                                            <div id="replyBox2" v-show="replyBox2" style="padding-bottom: 10px">
                                                <el-input
                                                        type="textarea"
                                                        :rows="2"
                                                        style="max-width: 80%"
                                                        placeholder="请输入内容"
                                                        v-model="textarea">
                                                </el-input>
                                                <el-button slot="append" :size="20"
                                                           style="margin-bottom: 5px;margin-left: 20px"
                                                           @click="replyComment">
                                                    回复评论
                                                </el-button>
                                                <el-link :underline="false">
                                                    <span @click="closeReplyInput2()" class="el-icon-arrow-up">收起</span>
                                                </el-link>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </el-card>
                        </div>

                    </el-tab-pane>
                    <el-tab-pane label="留言板的留言" name="third">留言板的留言</el-tab-pane>
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
                activeName: 'first', //标签头默认选择栏
                textarea: '',//回复框
                multipleSelection: [],
                delList: [],
                form: {},
                idx: -1,
                id: -1,
                comments: {},//博客文章评论
                commentList: {},//我的评论
                time: 0, //评论回复时间差
                replyBox: false, //回复框显示
                replyBox2: false, //回复框显示
                commentId: 0, //评论id
                blogId: 0, //博客id
                //储存评论数据
                comment: {
                    userId: 0,
                    content: '',
                    blogId: 0,
                    parentCommentId: -1,
                },

            };
        },

        methods: {
            /*我的文章评论*/
            //被选中的标签实例
            handleClick(tab, event) {
                const g_this = this;
                /*console.log(tab, event);*/
                /* console.log(tab.paneName, event);*/

                g_this.firstData(tab.paneName);
                //我发表的评论
                if (tab.paneName === 'second') {
                    const g_this = this;
                    let userId = g_this.$store.getters.getUser.id;
                    g_this.axios.get("/comment/userComment/" + userId).then(res => {
                        console.log(res.data.data + "我的评论----------")
                        g_this.commentList = res.data.data;
                    });
                }
            },
            //页面首显内容
            firstData(data) {
                const g_this = this;
                if (data === 'first') {
                    g_this.axios.get("/comment/blog/comment").then(res => {
                        console.log(res.data.data + "博客文章评论----------")
                        g_this.comments = res.data.data;
                    });
                }
            },
            //跳转博客详情
            toBlogDetail(blogId) {
                this.$router.push("/fblog/" + blogId);
            },
            //显示回复框
            reply(commentId, blogId) {
                this.commentId = commentId;
                this.blogId = blogId;
                this.replyBox = true;
                let replyId = commentId
                let box = document.getElementById('replyBox');
                document.getElementById(replyId).appendChild(box);
            },
            //显示回复框
            reply2(commentId, blogId) {
                this.commentId = commentId;
                this.blogId = blogId;
                this.replyBox2 = true;
                let replyId = commentId
                let box = document.getElementById('replyBox2');
                document.getElementById(replyId).appendChild(box);
            },
            //收起回复框
            closeReplyInput() {
                this.replyBox = false;
            },
            closeReplyInput2(){
                this.replyBox2 = false;
            },
            //回复评论
            replyComment() {
                if (!this.textarea) {
                    this.$message({
                        showClose: true,
                        type: 'warning',
                        message: '评论不能为空'
                    })
                } else {
                    let g_this = this;
                    let token = localStorage.getItem("token");
                    if (token) {
                        g_this.comment.userId = g_this.$store.getters.getUser.id;
                        g_this.comment.content = g_this.textarea;
                        g_this.comment.blogId = g_this.blogId
                        g_this.comment.parentCommentId = g_this.commentId
                        //储存
                        g_this.axios.post("/comment/save", g_this.comment, {
                            headers: {
                                "Authorization": localStorage.getItem("token")
                            }
                        }).then(res => {

                            g_this.$message.success('回复成功');
                            g_this.textarea = ''
                            g_this.firstData('first');
                            this.replyBox = false;
                        })
                    } else {
                        this.$message({
                            showClose: true,
                            type: 'warning',
                            message: '您需要登陆才能评论'
                        })
                    }
                }
            },
            //收起卡片
            closeCard(index) {
                let closeCard = index + 10085
                let closeCardBtn = document.getElementById(closeCard);
                closeCardBtn.style.display = "none";
                let openCard = index + 10086
                let openCardBtn = document.getElementById(openCard);
                openCardBtn.style.display = "";
                let commentCard = document.getElementById(index);
                commentCard.style.display = "none";
            },
            //恢复卡片
            openCard(index) {
                let closeCard = index + 10085
                let closeCardBtn = document.getElementById(closeCard);
                closeCardBtn.style.display = "";
                let openCard = index + 10086
                let openCardBtn = document.getElementById(openCard);
                openCardBtn.style.display = "none";
                let commentCard = document.getElementById(index);
                commentCard.style.display = "";
            },
            //删除评论
            confirm(commentId) {
                const g_this = this;
                g_this.axios.get("/comment/delete/" + commentId).then(res => {
                    setTimeout(() => {
                        g_this.$message.success('删除成功');
                        this.firstData('first');
                    }, 500);

                });

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
                this.firstData('first')
            }
        }


    };
</script>

<style scoped>
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
