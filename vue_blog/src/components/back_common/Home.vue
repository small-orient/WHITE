<template>
    <div class="wrapper">
        <v-head></v-head>
        <v-sidebar></v-sidebar>
        <div class="content-box" :class="{'content-collapse':collapse}">
            <v-tags></v-tags>
            <div class="content">
                <transition name="move" mode="out-in">
                    <keep-alive :include="tagsList">
                        <router-view></router-view>
                    </keep-alive>
                </transition>
                <el-backtop target=".content"></el-backtop>
            </div>
        </div>
    </div>
</template>

<script>
import vHead from './Header.vue';
import vSidebar from './Sidebar.vue';
import vTags from './Tags.vue';
import bus from './bus';
export default {
    data() {
        return {
            tagsList: [],
            collapse: false
        };
    },
    components: {
        vHead,
        vSidebar,
        vTags
    },
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
            bus.$on('collapse-content', msg => {
                this.collapse = msg;
            });

            // 只有在标签页列表里的页面才使用keep-alive，即关闭标签之后就不保存到内存中了。
            bus.$on('tags', msg => {
                let arr = [];
                for (let i = 0, len = msg.length; i < len; i++) {
                    msg[i].name && arr.push(msg[i].name);
                }
                this.tagsList = arr;
            });
        }

       /* if (this.$store.getters.getUser.id === 1){
            bus.$on('collapse-content', msg => {
                this.collapse = msg;
            });

            // 只有在标签页列表里的页面才使用keep-alive，即关闭标签之后就不保存到内存中了。
            bus.$on('tags', msg => {
                let arr = [];
                for (let i = 0, len = msg.length; i < len; i++) {
                    msg[i].name && arr.push(msg[i].name);
                }
                this.tagsList = arr;
            });
        }else {
            this.$message.error('您无权限访问！');
            this.$router.push("/fblog");
        }*/

    }
};
</script>
<style>

</style>
