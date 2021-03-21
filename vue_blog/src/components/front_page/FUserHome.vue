<template>
    <div class="user-background">
        <el-card class="" style="height: 70px;background-color: rgba(141,159,212,0.55) ">
            <el-col :span="24">
                <div style="text-align: center" >
                    <!--通过svg引入显示彩色-->
                    <el-link @click="isCollapse = !isCollapse"
                             style="float: left;"
                             type="primary"
                             :underline="false"
                    ><svg class="iconfont" aria-hidden="true"
                             style="font-size: 25px;margin-top: 5px;">
                            <use xlink:href="#icon-mianbaoxie1"></use>
                        </svg>
                        <span style="font-size:25px;padding-left: 5px;">WHITE'S(个人中心)</span>
                    </el-link>
                </div>
            </el-col>
        </el-card>
        <el-row class="tac">
            <el-col :span="4">
            <el-menu default-active="1-4-1"
                     class="el-menu-vertical-demo"
                     text-color="rgba(141,159,212)"
                     background-color="rgba(58,62,74,0.55)"
                     router
                     :collapse="isCollapse">
                <template v-for="item in items">
                    <template v-if="item.title==='评论查看'">
                        <el-menu-item :index="item.index" :key="item.index"
                                      @click="isCollapse = !isCollapse">
                            <i :class="item.icon"></i>
                            <span slot="title"  >
                                {{ item.title }}</span>
                        </el-menu-item>
                    </template>
                    <template v-else>
                        <el-menu-item :index="item.index" :key="item.index">
                            <i :class="item.icon"></i>
                            <span slot="title" >
                                {{ item.title }}</span>
                        </el-menu-item>
                    </template>
                </template>
            </el-menu>
            </el-col>

            <!--左边栏-->
           <!-- <el-col :span="4">
                <el-menu
                        class="f-header-el-menu"
                        :default-active="fMenu_user"
                        text-color="#0a0a0a"
                        active-text-color="#b2f1f1"
                        style="height: 800px;
                        background: rgba(255,255,255,0.9);"
                        router

                >


                </el-menu>
            </el-col>-->
            <!--右边栏-->
            <el-col :span="20">
                <router-view></router-view>
            </el-col>
        </el-row>
        <Footer></Footer>
    </div>
</template>

<script>
    import Footer from "../front_common/FrontFooter";

    export default {
        name: "FUser",
        components:{
         Footer
        },
        data(){
            return{
                isCollapse:false,
                items: [
                    {
                        icon: 'el-icon-user',
                        index: 'user',
                        title: '个人资料'
                    },
                    {
                        icon: 'el-icon-warning-outline',
                        index: 'personal',
                        title: '修改密码'
                    },
                    {
                        icon: 'el-icon-postcard',
                        index: 'comment',
                        title: '评论查看'
                    },
                    {
                        icon: 'el-icon-chat-line-round',
                        index: 'fwall',
                        title: '向博主留言'
                    },
                    {
                        icon: 'el-icon-back',
                        index: 'FBlog',
                        title: '返回首页'
                    },
                ]

            }

        },
        methods:{

        },
        computed: {
            fMenu_user() {
                return this.$route.path.replace('/', '');

            }
        },
    }
</script>

<style scoped>
    @media screen and (max-width: 992px){
        .user-background{

            /*background: url('../../assets/img/002.png') no-repeat center center fixed*/;
            background: url('https://img-blog.csdnimg.cn/20210309221322455.png') no-repeat center center fixed;
            height: 100%;
            position: fixed;
            width: 100%;
            overflow:auto;
        }
        .card{
            margin:0 7%;
            border-radius: 10px;
        }
    }


    @media screen and (min-width: 992px){
        .user-background{

            /*background: url('../../assets/img/003.png') no-repeat center center fixed ;*/
            background: url('https://img-blog.csdnimg.cn/20210309221329537.png') no-repeat center center fixed ;
            height: 100%;
            position: fixed;
            width: 100%;
            overflow:auto;
        }
        .card{
            border-radius: 5px;
        }
    }
    .f-header-el-menu {
        width: 200px;

    }

    .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 200px;
        min-height: 900px;
    }
</style>