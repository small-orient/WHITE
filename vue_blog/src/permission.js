//权限拦截

import router from "./router";


router.beforeEach((to,from,next)=>{
    //判断是否需要登录权限
    if (to.matched.some(record=>record.meta.requireAuth)){
        const  token = localStorage.getItem("token")
      /*  console.log("------------token"+token)*/

        if (token){
            //token存在
            if (to.path === '/login'){
                //url是login，不管
            }else {
                //不是，就放行
                next()
            }

        }else {
            //不存在，登录
            next({
                path:'/login'
            })
        }

    }else {
        next()
    }
})


