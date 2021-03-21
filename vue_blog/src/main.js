//兼容IE
import 'babel-polyfill'
import Es6Promise from 'es6-promise'
Es6Promise.polyfill();
import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import axios from 'axios';
import VueAxios from 'vue-axios'
import store from './store';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import './assets/css/theme-green/index.css'; // 浅绿色主题
import './assets/style/iconfont/iconfont.css' //使用icon
import './assets/style/iconfont/iconfont.js'
import './assets/css/icon.css';
import 'babel-polyfill';
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import './permission'
import Highlight  from './highlight' //导入代码高亮文件
import animated from 'animate.css' //动画
import "./axios.js"
import Router from "vue-router";





Vue.config.productionTip = false;
Vue.use(ElementUI, {
    size: 'small'
});

Vue.use(VueAxios, axios)
Vue.use(mavonEditor)

Vue.use(Highlight);

Vue.use(animated);

/*Vue.axios.get(api).then((response) => {
  console.log(response.data)
})

this.axios.get(api).then((response) => {
  console.log(response.data)
})*/

/*Vue.prototype.$axios = axios*/

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');


/*//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | vue-manage-system`;
    const role = localStorage.getItem('ms_username');
    if (!role && to.path !== '/login') {
        next('/login');
    } else if (to.meta.permission) {
        // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
        role === 'admin' ? next() : next('/403');
    } else {
        // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
        if (navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor') {
            Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
                confirmButtonText: '确定'
            });
        } else {
            next();
        }
    }
});*/


