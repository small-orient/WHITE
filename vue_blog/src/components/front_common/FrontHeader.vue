<template>
    <div class="background-header">
        <el-card class="navigation01"
                 style="height:100%;background-color: rgba(0,0,0,0.3)"
        >
    <div class="m-content">
        <div >
            <div >
                <div class="top-web" v-show="description">
                    <div style="padding-top: 20px" class="animated fadeInDown">
                        <div>
                                <!--自我介绍-->
                                <div class="avatar-blogs">
                                    <el-avatar :size="avatarState"
                                               :src="blogger.avatar">
                                    </el-avatar>
                                </div>

                        </div>
                        <span class="introduce">
                    欢迎来到白的兔的小站</span>
                    </div>
                    <div style="padding-top: 20px" class="animated fadeInUp">
                        <span style="font-size: 30px;
                        color: #ffffff;
                        font-family: STSong,serif;" >
                          小小方块世界
                        </span>
                    </div>
                    <div style="padding-top: 20px" class="animated fadeInUp">
                        <span style="font-size: 30px;
                        color: #ffffff;
                        font-family: STSong,serif;" >
                             欢迎到访
                        </span>
                    </div>
                    <div  class="animated fadeInUp">
                        <span style="font-size: 30px;
                        color: #ffffff;
                        font-family: STSong,serif;" >
                             ...
                        </span>
                    </div>
                </div>
                <div class="top-position" style="padding-top: 150px" v-show="position"></div>
            </div>
    <div class="header-area">
        </div>
        <div class="functionColumn" v-show="navigation01">
            <el-divider direction="vertical"></el-divider>
            <el-button style="background-color: rgba(252,250,250,0.2)" @click="Fhome" >
                <el-link :underline="false" type="primary"
                           style="font-size: 16px;color: rgb(75,252,255)">
                    &nbsp;&nbsp;&nbsp;&nbsp;首页&nbsp;&nbsp;&nbsp;&nbsp;</el-link></el-button>
            <el-divider direction="vertical"></el-divider>
            <el-button style="background-color: rgba(252,250,250,0.2)"  @click="Ftype">
                <el-link :underline="false" type="primary"  style="font-size: 16px;color: rgb(75,252,255)">
                     &nbsp;&nbsp;&nbsp;&nbsp;文章&nbsp;&nbsp;&nbsp;&nbsp;</el-link></el-button>
            <el-divider direction="vertical" ></el-divider>
            <el-button style="background-color: rgba(252,250,250,0.2)" @click="Ffarchive" >
                <el-link :underline="false" type="primary" style="font-size: 16px;color: rgb(75,252,255)">
                     &nbsp;&nbsp;时间轴&nbsp;&nbsp;</el-link></el-button>
            <el-divider direction="vertical" ></el-divider>

            <!--过审隐藏-->
            <el-button style="background-color: rgba(252,250,250,0.2)" @click="Fwall">
            <el-link :underline="false" type="primary"  style="font-size: 16px;color: rgb(75,252,255)">
                 &nbsp;&nbsp;留言墙&nbsp;&nbsp;</el-link></el-button>
            <el-divider direction="vertical" ></el-divider>

            <el-button style="background-color: rgba(252,250,250,0.2)"  @click="link">
                <el-link :underline="false" type="primary"  style="font-size: 16px;color: rgb(75,252,255)">
                    &nbsp;&nbsp;&nbsp;&nbsp;友链&nbsp;&nbsp;&nbsp;&nbsp;</el-link></el-button>
            <el-divider direction="vertical" ></el-divider>
            <el-button style="background-color: rgba(252,250,250,0.2)">
                <el-link type="primary" :underline="false" @click="About"
                          style="font-size: 16px;color: rgb(75,252,255)">
                    &nbsp;&nbsp;关于我&nbsp;&nbsp;</el-link></el-button>
            <el-divider direction="vertical"></el-divider>

            <!--没登录显示-->
            <!--过审隐藏-->
            <el-button style="background-color: rgba(252,250,250,0.2)" v-show="!hasLogin" @click="frontLogin">
                <el-link :underline="false" style="font-size: 16px;color: rgb(75,252,255)">
                    &nbsp;&nbsp;&nbsp;&nbsp;登录&nbsp;&nbsp;&nbsp;&nbsp;</el-link></el-button>

            <!--登录显示-->
            <el-dropdown  v-show="hasLogin"
                          class="user-name"
                          trigger="click"
                          @command="logout"
            >
                    <el-link :underline="false" class="el-dropdown-link">
                        <i class="el-icon-caret-bottom" style="font-size: 15px;color: rgb(75,252,255)">
                            个人信息({{user.nickName}})</i>
                    </el-link>
                <el-dropdown-menu slot="dropdown" v-if="user.id === 1">
                    <el-link @click="goBack">
                        <el-dropdown-item>回到后台</el-dropdown-item>
                    </el-link>
                    <el-dropdown-item>
                        <el-link  @click="goUser">个人信息 </el-link>
                    </el-dropdown-item>
                    <el-dropdown-item divided  command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
                <el-dropdown-menu v-else slot="dropdown">
                    <el-link  @click="goUser">
                        <el-dropdown-item>个人资料</el-dropdown-item>
                    </el-link>
                    <el-dropdown-item divided >
                        <el-link @click="goComment" >我的消息</el-link>
                    </el-dropdown-item>
                    <el-dropdown-item divided>
                        <el-link @click="goUpdatePwd" >修改密码</el-link></el-dropdown-item>
                    <el-dropdown-item divided  command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <!--头像-->
            <!--昵称-->
            <el-avatar :size="50"
                       v-show="hasLogin"
                       :src="user.avatar">
            </el-avatar>
        </div>
        </div>
        </div>
        </el-card>
        <!--小屏目录-->
        <!--顶部卡片-->
        <el-card class="box-card" style="height: 70px;background-color: rgba(58,62,74,0.55)">
            <el-row>
                <!--面包标签:引出抽屉-->
                <el-col :span="6">
                    <div style="margin-right: 60%;" >
                        <!--通过svg引入显示彩色-->
                        <el-button @click="drawer = true"
                                   size="small"
                                   class="el-icon-d-arrow-right"
                                   style="background-color: rgba(58,62,74,0.33)"
                        >

                        </el-button>
                    </div>
                </el-col>
                <!--博客标题-->
                <el-col :span="18">
                    <div>
                <span style="font-size:40px;padding-left: 18%;color: white">WHITE'S</span>
                    </div>
                </el-col>
            </el-row>
        </el-card>
        <!--目录抽屉-->
        <el-drawer
                :show-close=false
                :visible.sync="drawer"
                :direction="direction"
                wrapperClosable="true"
                append-to-body="true"
                style="overflow: auto;opacity:0.85 !important;padding-top: 70px"
        >
            <el-row class="tac">
                <el-col :span="24">
                    <el-menu
                            class="f-header-el-menu"
                            :default-active="fMenu"
                            text-color="#0a0a0a"
                            active-text-color="#b2f1f1"
                            router
                            @select="drawer=false"
                    >
                        <!--登陆时显示样式-->
                        <div v-show="header_menu">
                            <div style="padding-bottom: 8px">
                            <span slot="title"
                                  style="font-size: 15px;padding-left: 20px;line-height: 1.8"
                            >{{user.nickName}},</span>

                                <div style="font-size: 15px;padding-left: 20px"
                                >欢迎来到WHITE'S !</div>
                            </div>
                            <template v-for="(item,index) in items" v-if="index<6">
                                <template >
                                    <el-menu-item :index="item.index" :key="item.index">
                                        <i :class="item.icon"></i>
                                        <span slot="title">{{ item.title }}</span>
                                    </el-menu-item>
                                </template>
                            </template>
                            <template v-for="(item,index) in items" v-if="index>7">
                                <template v-if="item.title === '退出登录'">
                                    <el-menu-item :index="item.index" :key="item.index"
                                                  @click="logout('logout')">
                                        <i :class="item.icon"></i>
                                        <span slot="title">{{ item.title }}</span>
                                    </el-menu-item>
                                </template>
                                <!--如果是博主，则回到真正后台-->
                                <template v-else-if="user.id === 1">
                                    <template v-if="item.title === '个人中心'">
                                        <el-menu-item :index="'/blogs'" :key="'/blogs'">
                                            <i :class="item.icon"></i>
                                            <span slot="title">{{ item.title }}</span>
                                        </el-menu-item>
                                    </template>
                                </template>
                                <template v-else>
                                    <el-menu-item :index="item.index" :key="item.index">
                                        <i :class="item.icon"></i>
                                        <span slot="title">{{ item.title }}</span>
                                    </el-menu-item>
                                </template>
                            </template>
                        </div>
                        <!--未登陆时显示样式-->
                        <div v-if="header_menu===false">
                            <template v-for="(item,index) in items" v-if="index<8">
                                <template >
                                    <el-menu-item :index="item.index" :key="item.index">
                                        <i :class="item.icon"></i>
                                        <span slot="title">{{ item.title }}</span>
                                    </el-menu-item>
                                </template>
                            </template>
                        </div>
                    </el-menu>
                </el-col>
            </el-row>
        </el-drawer>
    </div>
</template>

<script>
    //公共：公用头页面
    export default {
        name: "FrontHeader",
        components: {
        },
        data () {
            return {
                avatarState:130,//博主头像大小
                blogger:{
                    //博主头像
                    avatar:'',
                },
                avatarImg:true,
                position:true,
                description:true,
                navigation01:true,
                //抽屉属性
                drawer: false,//控制抽屉开关
                direction: 'ltr', //左边开启
                //用户信息
                user:{
                    nickName:'',
                    userName:'',
                    avatar:'',
                    id:0,
                },
                hasLogin:false, //根据登陆情况显示登陆连接
                header_menu:false, //根据登陆情况显示菜单
                items: [
                    {
                        icon: 'el-icon-lx-home',
                        index: '/fblog',
                        title: '首页'
                    },
                    {
                        icon: 'el-icon-lx-copy',
                        index: '/ftype',
                        title: '文章'
                    },
                    {
                        icon: 'el-icon-lx-cascades',
                        index: '/farchive',
                        title: '时间轴'

                    },
                    {
                        icon: 'el-icon-lx-calendar',
                        index: '/fwall',
                        title: '留言墙'

                    },
                    {
                        icon: 'el-icon-info',
                        index: '/about',
                        title: '关于我'

                    },
                    {
                        icon: 'el-icon-connection',
                        index: '/link',
                        title: '友情链接'

                    },
                    {
                        icon: 'el-icon-thumb',
                        index: '/login',
                        title: '登录'

                    },
                    {
                        icon: 'el-icon-edit-outline',
                        index: '/fregistered',
                        title: '注册'

                    },
                    {
                        icon: 'el-icon-user',
                        index: '/user',
                        title: '个人中心'

                    },
                    {
                        icon: 'el-icon-bicycle',
                        index: '/',
                        title: '退出登录'

                    },
                ]

            }
        },
        methods:{
            //退出
            logout(command) {
                if (command === "logout") {
                    const g_this = this;
                    //后端清除数据
                    g_this.axios.post("/user/logout", '',{
                        headers: {
                            "Authorization": localStorage.getItem("token")
                        }
                    }).then(res => {
                        //前端清除用户信息
                        g_this.$store.commit("REMOVE_INFO");
                        //如果退出，则显示'登录'
                        g_this.hasLogin = false;
                        g_this.header_menu = false;
                        g_this.user.nickName = '';
                        g_this.$router.push('/fblog');

                    })
                }
            },
            /*宽屏导航栏路由*/
            frontLogin(){
                this.$router.push("/login");
            },
            Fhome(){
                this.$router.push("/fblog");
                this.description = true;
            },
            Ftype(){
                this.$router.push("/ftype");
                this.description = false;

            },
            Ffarchive(){
                this.$router.push("/farchive");
                this.description = false;

            },
            Fwall(){
                this.$router.push("/fwall");
                this.description = false;
            },
            About(){
                this.$router.push("/about");
                this.description = false;
            },
             link(){
                this.$router.push("/link");
                this.description = false;
            },
            //回到后台
            goBack(){
                this.$router.push("/blogs");
            },
            //个人信息页面
            goUser(){
                this.$router.push("/user");
            },
            //修改密码
            goUpdatePwd(){
                this.$router.push("/personal");
            },
            //个人消息
            goComment(){
                this.$router.push("/comment");
            },


        },
        computed: {
            fMenu() {
                return this.$route.path.replace('/', '');

            }
        },
        created() {
            const g_this = this;
            //获取博主头像
            this.axios.get("/user/bloggerAvatar").then(res =>{
                g_this.blogger = res.data.data;
            })

            //从store中获取用户信息
            if (this.$store.getters.getUser !== null
                && this.$store.getters.getUser !== undefined
                && this.$store.getters.getUser !== ""){
                //后台获取用户信息
                g_this.axios.get("/user/owner",{
                    headers:{
                        "Authorization":localStorage.getItem("token")}
                }).then((res) => {
                    g_this.user.avatar =  res.data.data.avatar;
                    g_this.user.nickName = res.data.data.nickName;
                    g_this.user.id = this.$store.getters.getUser.id
                })

                //如果登录，则显示个人信息
                this.hasLogin = true;
                this.header_menu = true;
            }
        } ,

    }
</script>

<style scoped>


    @media screen and (max-width: 992px){
        .m-content{
            max-width: 960px;
            margin: 0 auto;
            margin-bottom: 10px;
            text-align: center;
        }
        .header-area{
                display: none;
        }
        .top-web{
           display: none;
        }
        .top-position{
            display: none;
        }
        .functionColumn{
            display: none;
        }
        .navigation01{
            display: none;
        }
    }

    @media screen and (min-width: 992px){
        .m-content{
            max-width: 1080px;
            margin: 0 auto;
            margin-bottom: 10px;
            text-align: center;
        }
        .background-header{
            /*background: url('../../assets/img/004.png') no-repeat center center fixed;*/
            background: url('https://img-blog.csdnimg.cn/20210309221407663.png') no-repeat center center fixed;
        }

        .box-card{
            display: none;
        }

        .top-web{
            height: 300px;
        }
    }

    .introduce{
        font-size: 35px;
        color: #ffffff;
        font-family: STXihei,serif;
    }

    .functionColumn{
        margin: 10px 0;
        font-size: 16px;
    }
    .f-header-el-menu {
        width: 300px;

    }
</style>