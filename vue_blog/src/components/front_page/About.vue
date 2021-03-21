<template>
    <div class="wall-content">
        <!--头部导航栏-->
        <div style="margin: 20px 3%" class="card">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }"><i style="color: rgb(234,236,241)">首页</i></el-breadcrumb-item>
                <el-breadcrumb-item ><i style="color: rgb(234,236,241)">关于我</i></el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <el-card class="box-card card animated fadeInUp "
                 style="background:rgba(255,255,255,0.96);min-height: 800px">
        <el-row>
            <el-col :span="24" >
            <div style="text-align: center">
                <span style="font-size: 35px;font-family:'宋体',sans-serif;">关于我</span>
            </div>
            <div
                style="text-align: center;
                        margin-top: 20px;
                        margin-bottom: 10px;
                        color: rgba(11,11,11,0.48)" >
                <span id="Date" class="el-icon-time">
                    &nbsp;{{time.year+'年'+time.mon+'月'+time.da+'日'+time.h+':'+time.m+':'+time.s}}</span>
            </div>
            </el-col>
        </el-row>
            <el-divider ></el-divider>

            <template>
                <div style="text-align: center">
                    <!--主体内容-->
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
                    <div style="padding-top: 10px;padding-bottom: 10px;margin-left: 10%;margin-right: 10%" class="animated fadeInLeft">
                        <span style="font-size: 16px;font-family:'宋体',sans-serif;" >
                            偶然间发现，学习过程中写的笔记无处放置，写在各大博客网站上，文笔也稍显幼稚。所以借此站写一些随笔。
                        </span>
                    </div>
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
                                           style="font-size: 30px;"></i>
                                    </span>
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
                            ></el-link>
                        </el-tooltip>
                        <el-tooltip content="去留言~" placement="top" style="padding-bottom: 6px">
                            <el-link
                                    type="info"
                                    class="iconfont icon-liuyan4"
                                    style="font-size: 30px;padding-left:10px"
                                    :underline="false"
                                    onclick="goToWall"
                            ></el-link>
                        </el-tooltip>
                    </div>
                    <el-divider content-position="center">网站简介</el-divider>
                    <div style="padding-top: 10px;padding-bottom: 10px;margin-left: 10%;margin-right: 10%" class="animated fadeInRight">
                        <span style="font-size: 14px;font-family:'宋体',sans-serif;line-height: 2">
                            &nbsp;&nbsp;WHITE'S
                            是一个基于前后端分离开发的个人网站,
                            前端使用vue+elementUI(不得不吐槽，每次写前端各类样式css真是极其消耗精力),
                            后端使用SpringBoot+MybatisPlus+Redis,
                            登录验证与部分权限校验使用Shiro+JWT。
                        </span>
                    </div>
                    <el-divider content-position="center">版本信息</el-divider>
                    <div style="padding-top: 10px;padding-bottom: 10px;margin-left: 10%;margin-right: 10%" class="animated fadeInLeft">
                        <h3>
                            第一版
                        </h3>
                        <span style="font-size: 14px;font-family:'宋体',sans-serif;line-height: 2">
                            &nbsp;&nbsp;这是本人目前所做的第一个前后端分离项目。
                            网上各大教程所写个人网站少有前后端分离（很可能是我找不到），
                            而本人则很想尝试前后端分离开发的实际状况,
                            于是立刻着手。
                            参照了许多大佬的网站样式（ApeNixX、ONESTAR等），
                            结合了自己的想法,
                            但部分样式做不出良好效果，
                            一些样式效果需要变换成vue的方式写入，
                            个人也是vue新手村成员，不能完全重现。
                            除开样式外，基本功能模块已经实现（发布文章模块、评论模块、用户管理模块、登录校验等）。
                            其中在用户模块使用了阿里云短信接口。
                        </span>
                        <h3>
                            第二版
                        </h3>
                        <span style="font-size: 14px;font-family:'宋体',sans-serif;line-height: 2">
                            部分内容加入redis缓存，且后端接口进行安全升级。
                        </span>
                    </div>
                    <el-divider content-position="center">下一步</el-divider>
                    <div style="padding-top: 10px;padding-bottom: 10px;margin-left: 10%;margin-right: 10%" class="animated fadeInRight">

                        <span style="font-size: 14px;font-family:'宋体',sans-serif;line-height: 2">
                            &nbsp;&nbsp;网站除了会持续更新随笔外，版本也会不间断的进行更新，完善体验。
                        </span>
                    </div>
                    <el-divider content-position="center">待续</el-divider>
                </div>
            </template>
        </el-card>
    </div>

</template>

<script>
    import qq from "../../assets/img/1329440214QQ.jpg";
    import weixin from "../../assets/img/weixin.png";


    /**
     * 留言墙
     *
     */
    export default {
        name: "FWall",
        data(){
            return{
                avatarState:200,//控制头像大小
                fit:'fill',
                bloggerAvatar:'',
                qqUrl: qq,
                weixinUrl: weixin,
                blogger:{
                    avatar:'',//博主头像
                    nickName:'',//博主昵称
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

            }
        },
        methods:{
            //去留言板
            goToWall(){
                this.$router.push("/fwall");
            },
        },
        created() {

            const g_this = this;
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
        .avatar-blogs{
            padding-top: 10px;
            padding-bottom: 10px;
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

        .avatar-blogs{
            padding-bottom: 10px;
            padding-top: 10px
        }
        }


</style>