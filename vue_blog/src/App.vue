<template>
    <div id="app">
        <router-view></router-view>

    </div>
</template>
<script>

    export default {
        name: 'app',
        data() {
            return {};
        },

        mounted(){

            //关闭浏览器，立即清空token。
            window.onbeforeunload = function () {
                let userInfo = window.sessionStorage.getItem("userInfo");
                if (userInfo===null ||userInfo===''||userInfo===undefined){
                    window.localStorage.removeItem("token");
                }
            }
        },
    }

</script>
<style>
    @import "./assets/css/main.css";
    @import "./assets/css/color-dark.css";     /*深色主题*/
    /*@import "./assets/css/theme-green/color-green.css";   浅绿色主题*/

    .iconfont {
        width: 1em; height: 1em;
        vertical-align: -0.15em;
        fill: currentColor;
        overflow: hidden;
    }
   /* #app{
        width: 100%;
        height: 100%;
        overflow:auto;
    }*/


</style>