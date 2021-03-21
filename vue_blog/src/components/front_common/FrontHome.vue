<template>
    <div class="background" >
   <!-- <div class="background" @scroll="handleScroll">-->
        <el-backtop target=".background"
                    style="background-color: rgba(240, 243, 242, 0.9);" >
            <img v-show="img1"
                 class="img1"
                 src="../../assets/_tortoise.svg"
                 @click="noLeg"
                 alt="加载..">

            <img v-show="img2"
                 class="img2"
                 @click="noLeg"
                 src="../../assets/tortoise.svg" alt="">
        </el-backtop>
        <Header id="header"></Header>
        <router-view></router-view>
        <Footer></Footer>

    </div>

</template>

<script>
    import Header from "./FrontHeader";
    import Footer from "./FrontFooter";
    import Blogs from "../front_page/FBlogs";
    import BlogType from "../front_page/FBlogType";

    //公共：博客列表页面
    export default {
        name: "FrontHome",
        data() {
            return {
                isRouterAlive:true,
                query: {
                    //分页信息
                    currentPage: 1,
                    pageSize: 9,
                    pageTotal: 0
                },
                url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
                srcList: [
                    'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
                    'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg'
                ],
                img1:true,
                img2:false,
                scrollTop:0,
            }
        },
        methods:{
            //切换图片
            noLeg(){
                const g_this = this
                g_this.img1 =  !g_this.img1;
                g_this.img2 =  !g_this.img2;

                /*改回来*/
                setTimeout(function () {
                    g_this.img2 =  false
                    g_this.img1 =  true
                },500)
            },
           /* //滚动条距离页面的距离
            handleScroll (el) {
                this.scrollTop = this.$refs.content.scrollTop
                console.log(this.scrollTop+"页面滚动距离++++++++++++")
            },*/
        },

        components: {Header,Footer,Blogs,BlogType}
    }
</script>

<style scoped>
.background{
    background-size: cover;
/*
    background: url('../../assets/img/leaves-pattern.png') center center repeat;
*/
    background:
            url('https://img-blog.csdnimg.cn/20210309221310666.png') no-repeat center center fixed;
    height: 100%;
    position: fixed;
    width: 100%;
    overflow:auto;
}

.img1 {
    width: 60px;
    height: 60px;
}

.img2 {
    width: 60px;
    height: 60px;
}



</style>