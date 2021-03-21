<template>
    <div class="wall-content">
        <!--头部导航栏-->
        <div style="margin: 20px 3%" class="card">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }"><i style="color: rgb(234,236,241)">首页</i></el-breadcrumb-item>
                <el-breadcrumb-item ><i style="color: rgb(234,236,241)">友情链接</i></el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <el-card class="box-card card animated fadeInUp "
                 style="background:rgba(255,255,255,0.90);min-height: 800px">
        <el-row>
            <el-col :span="24" >
            <div style="text-align: center">
                <span style="font-size: 35px;font-family:'宋体',sans-serif;" class="el-icon-link">
                    友情链接</span>
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
            <el-divider ></el-divider>
            <!--添加友链声明-->
            <h3 style="padding-left: 10%;font-family:'宋体',sans-serif;">添加声明</h3>
            <div style="padding-left: 20%;padding-top: 15px">
            <ul>
                <li><span style="font-family:'宋体',sans-serif;line-height: 2">无违法、色情、敏感内容</span></li>
                <li><span style="font-family:'宋体',sans-serif;line-height: 2">网站可长期访问</span></li>
                <li><span style="font-family:'宋体',sans-serif;line-height: 2">原创、技术文章优先</span></li>
                <li><span style="font-family:'宋体',sans-serif;line-height: 2">
                    友链互换需提供下列格式，同时将本站添加至你的友链列表。可在留言板中留下友链信息。</span></li>
            </ul>
            </div>
            <h3 style="padding-left: 10%;font-family:'宋体',sans-serif;padding-top: 20px">申请格式</h3>
            <div style="padding-left: 20%;padding-top: 15px">
                <ul>
                    <li><span style="font-family:'宋体',sans-serif;line-height: 2">名称：WHITE'S</span></li>
                    <li><span style="font-family:'宋体',sans-serif;line-height: 2">地址：https://www.baidetu.cn/</span></li>
                    <li><span style="font-family:'宋体',sans-serif;line-height: 2">图片地址：https://www.blog.baidetu.cn/image/headImg/1606064521969.gif</span></li>
                </ul>
            </div>
            <el-divider content-position="center">友链中心</el-divider>
                    <div class="link-div">
                     <el-link  v-for="link in linkList"
                               @click="toLink(link.linkUrl)"
                               :underline="false"
                     >
                    <el-card shadow="hover"
                             class="link-card"
                             style="float: left;margin-left: 10px"
                    >
                        <el-image
                                :src="link.linkImg"
                                style="max-width: 175px; max-height: 125px;min-width: 50px;min-height: 50px"
                                fit="fill"
                        >
                        </el-image>
                        <div style="padding: 14px;text-align: center">
                            <span style="font-weight: bold;font-size: 16px;">{{link.linkName}}</span>
                        </div>
                    </el-card>
                     </el-link>
                    </div>
        </el-card>
    </div>

</template>

<script>


    /**
     * 友链
     *
     */

    export default {
        name: "FWall",
        data(){
            return{
                avatarState:200,//控制头像大小
                fit:'fill', //图片显示样式
                //显示时间
                time:{
                    year:0,
                    mon:0,
                    da:0,
                    h:0,
                    m:0,
                    s:0,
                },

                blogger:{
                    avatar:'',//博主头像
                    nickName:'',//博主昵称
                },

                linkList:{}
            }
        },
        methods:{
            toLink(url){
                window.open(url,'_blank');
            }
        },
        created() {
            const g_this = this;
            g_this.axios.get("/link/list").then(res=>{
                g_this.linkList = res.data.data;
            })

            //获取博主头像
            this.axios.get("/user/bloggerAvatar").then(res =>{
                g_this.blogger= res.data.data;
            })

            //实时时间显示
            setInterval(function(){
                const date = new Date();
                g_this.time.year=date.getFullYear(); //获取当前年份
                g_this.time.mon=date.getMonth()+1; //获取当前月份
                g_this.time.da=date.getDate(); //获取当前日
                g_this.time.h=date.getHours(); //获取小时
                g_this.time.m=date.getMinutes(); //获取分钟
                g_this.time.s=date.getSeconds(); //获取秒
              },0)
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
        .link-card{
            width: 150px;
            margin-top: 20px;
            height: 100px;
        }
        .link-div{
            margin-left: 0.8%;
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

        .link-card{
            width: 200px;
            margin-top: 20px;
            height: 200px;
        }

        .link-div{
            margin-left: 1.5%;
        }
        }


    /**
    友链CSS
     */
    .time {
        font-size: 13px;
        color: #999;
    }

    .bottom {
        margin-top: 13px;
        line-height: 12px;
    }

    .link-card{
        background:rgba(255,255,255,0.3);
        border-radius: 10px;
        margin-bottom: 20px;
    }

    .image {
        width: 100%;
    }


</style>