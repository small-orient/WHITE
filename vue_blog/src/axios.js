import axios from 'axios';
import ElementUI from 'element-ui';
import store from './store';
import router from './router';

/*axios配置前后拦截处理*/

//拦截请求路径
//axios.defaults.baseURL = "https://www.blog.baidetu.cn";
axios.defaults.baseURL = "http://localhost:8081";

//前置拦截
axios.interceptors.request.use(config => {
    return config;
});
//后置拦截
axios.interceptors.response.use(response => {
    //进入axios路径后，获取返回信息
    let res = response.data;

  /*  console.log("==============")
    console.log(res)
    console.log("==============")*/

    if (res.code === 200) {
        //信息正常，回到相应组件继续操作
        return response;
    } else {
        //错误，则弹出错误信息
       /* ElementUI.Message.error('用户信息错误！', {duration: 3 * 1000});*/
        ElementUI.Message.error(response.data.msg, {duration: 3 * 1000});
        //阻止进入响应的组件，返回自定义错误信息在控制台
        return Promise.reject(response.data.msg)
    }
}, error => {
    //后端直接报异常话，捕捉对应异常做处理
    console.log(error)
    console.log(error.message)
    if (error.response.data) {
        //获取后端报异常返回的错误信息
        error.message = error.response.data.msg
    }

    if (error.response.status === 401) {
        //
        store.commit("REMOVE_INFO");
        router.push("/fblog")
    }


    //弹出错误信息
    /*ElementUI.Message.error(error.message, {duration: 3 * 1000});*/
    return Promise.reject(error)


});
