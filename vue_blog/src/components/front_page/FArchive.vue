<template>
    <div class="archive-content">
        <!--头部导航栏-->
        <div style="margin: 20px 3%" class="card">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/' }"><i style="color: rgb(234,236,241)">首页</i></el-breadcrumb-item>
                <el-breadcrumb-item>
                    <i style="color: rgb(234,236,241)">归档</i></el-breadcrumb-item>
            </el-breadcrumb>
        </div>
       <!--时间轴-->
       <div class="time-line" style="min-height: 800px">
           <div class="section">
               <ul>
                   <li v-for="(blog,index) in blogArchive" :key="index">
                       <div class="line"></div>
                       <div class="item-wrapper animated fadeIn">
                           <div class="circle"></div>
                           <div class="across-line"></div>
                           <div class="font_18 gray_color"
                                style="text-align: center; border:1px solid #ffc416;
                                    background-color:#ffb914;
                                    border-radius :6px;
                                    width: 100px;
                                    margin-left: 20px;
                                   position:relative;
                                   ">
                               {{blog.createTime}}
                           </div>
                           <div class="item-content">
                               <div class="titleCard">
                                        <router-link :to="{name:'FBlogDetail',params:{blogId:blog.id}}">
                                            <span style="color: rgb(58,62,74)">{{blog.title}}</span>
                                        </router-link>
                               </div>
                           </div>
                       </div>
                   </li>
               </ul>
           </div>
       </div>
       <!-- <div class="doc-container m-padded-tb-massivex m-opacity" id="doc-container">
                <section class="timeline">
                    <ul>
                        <li class="" v-for="blog in blogArchive">
                            <div>
                                <time> {{blog.createTime}}</time>
                                <div class="scientist" >
                                    <a href="#" target="_blank" >
                                        <h3 class="state" style="text-align:center;font-size: 16px;color: #000;">
                                            {{blog.title}}</h3>
                                    </a>
                                </div>
                            </div>
                        </li>

                    </ul>
                </section>
        </div>-->

   </div>
</template>

<script>
    /**
     * 时间轴
     */
    export default {
        name: "FArchive",
        data() {
            return {
                activities: [{
                    content: '支持使用图标--------------------------',
                    timestamp: '2018-04-12 20:46',
                    size: 'large',
                    type: 'primary',
                }],
                blogArchive:{},


            }
        },
        methods:{


        },
        created() {
           /*获取时间轴*/
            let g_this = this;
            g_this.axios.get("/archive/archives").then(res=>{
              /*  console.log(res+"博客时间轴--------------")*/
                g_this.blogArchive=res.data.data;
            })


        }
    }
</script>

<style scoped lang="stylus">

    @media screen and (max-width: 992px){
        .archive-content{
            max-width: 960px;
            margin: 1px auto;

        }
        .card{
            margin:0 7%;
            border-radius: 5px;
        }
        .titleCard{
            color: #040c12;
            text-align: center;
            padding: 20px 20px;
            margin :5% 10%;
            box-sizing :border-box;
            border :1px solid #FFF;
            background-color:#FFF;
            box-shadow:0 4px 8px 0 rgba(0,0,0,0.05),0 6px 20px 0 rgba(0,0,0,0.01);
            border-radius: 6px;
            background:rgba(255,255,255,0.8);
        }
    }


    @media screen and (min-width: 992px){
        .archive-content{
            max-width: 1080px;
            margin: 1px auto;
        }
        .card{
            border-radius: 5px;
        }
        .titleCard{
            color: #040c12;
            text-align: center;
            padding: 30px 20px;
            box-sizing :border-box;
            border :1px solid #FFF;
            background-color:#FFF;
            box-shadow:0 4px 8px 0 rgba(0,0,0,0.05),0 6px 20px 0 rgba(0,0,0,0.01);
            border-radius: 6px;
            background:rgba(255,255,255,0.8);
        }
    }

    //时间轴CSS
    .section {
        max-width 600px
        margin 40px auto
        padding-left 50%
        li {
            box-sizing border-box
            position relative
            padding-bottom 20px
            list-style-type:none;
            .line {
                position absolute
                left 0
                height 100%
                border-left 1px solid pink
            }
            .item-wrapper {
                position relative
                .circle {
                    width 10px
                    height 10px
                    border 2px solid pink
                    background pink
                    border-radius 50%
                    position absolute
                    top 0
                    bottom 0
                    margin-top auto
                    margin-bottom auto
                }
                .across-line {
                    width 16.5%
                    height 1px
                    background pink
                    position absolute
                    top 0
                    bottom 0
                    margin-top auto
                    margin-bottom auto
                }
                .item-content {
                    padding 20px 20px
                    box-sizing border-box
                    border 1px solid #FFF
                    background-color: #FFF;
                    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.03);
                    border-radius 6px
                    background:rgba(255,255,255,0.5);
                }
            }
            &:nth-child(odd) {
                .item-wrapper {
                    left -100%
                    @media screen and (max-width: 992px){
                        padding-right 20%
                        padding-left 8%
                    }
                    @media screen and (min-width 992px) {
                        padding-right 25%
                        padding-left 2%
                    }

                    .circle {
                        right -7px
                    }
                    .across-line {
                        right 7px
                    }
                }
            }

            &:nth-child(even) {
                .item-wrapper {
                    @media screen and (max-width: 992px){
                        padding-left 20%
                        padding-right 8%
                    }
                    @media screen and (min-width 992px) {
                        padding-left 25%
                        padding-right 2%
                    }
                    .circle {
                        left -7px
                    }
                    .across-line {
                        left 7px
                    }
                }
            }
        }

    }



</style>