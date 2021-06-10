import Vue from 'vue'
import Vuex from 'vuex'


Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        token: '',
        //4.可以给userInfo一个初始化值，从sessionStorage里面拿,token也可以，但先不写了
        userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
        address:'http://localhost:8081/',
        //address:'https://www.blog.baidetu.cn/',
    },
    mutations: {
        //相当于set方法
        //1.将登录时获得的token保存，以便所有组件可以使用
        SET_TOKEN: (state, token) => {
            state.token = token;
            //将token也保存到浏览器的localStorage中
            localStorage.setItem("token", token);

        },
        //2.将登录时获得的userInfo用户信息保存，以便所有组件可以使用
        SET_USERINFO: (state, userInfo) => {
            state.userInfo = userInfo;
            //将userInfo也保存到浏览器的sessionStorage中,
            // sessionStorage在浏览器关闭的时候丢失，所以再打开是没有用户信息的，
            // 但是localStorage有token，可以通过token能够再次获取用户信息.
            //可以在APP.vue页面写一个方法，判断有没有sessionStorage的用户信息，没有就调用接口登录.
            sessionStorage.setItem("userInfo", JSON.stringify(userInfo));
        },
        //3.使用REMOVE_INFO时，会将token与userinfo的信息删除.
        REMOVE_INFO: (state) => {
            state.token = '';
            state.userInfo = {};
            //浏览器数据也要清空
            localStorage.setItem("token", '');
            sessionStorage.setItem("userInfo", JSON.stringify(""));
        },
        //前端保存全局访问变量
        // SAVE_ADDRESS:(state,address)=>{
        //     state.address = address;
        // }
    },
    getters: {
        //相当于get方法
        //5.获取用户信息,其实其他组件通过state.token这种方法也可以直接获取信息的
        getUser: state => {
            return state.userInfo;
        },
        //获取访问地址
        getAddress: state => {
            return state.address;
        },

    },

    actions: {},
    modules: {}
})
