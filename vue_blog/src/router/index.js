import Vue from 'vue';
import Router from 'vue-router';
import FrontHome from '../components/front_common/FrontHome.vue';


Vue.use(Router);
//消除浏览器由于路由重复错误报错
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

/*Router.onError((error) => {
    const pattern = /Loading chunk (\d)+ failed/g;
    const isChunkLoadFailed = error.message.match(pattern);
    const targetPath = Router.history.pending.fullPath;
    if (isChunkLoadFailed) {
        Router.replace(targetPath);
    }
});*/

const router = new Router({
  /*  mode:'history',*/
    routes: [
        {
            path: '/',
            redirect: '/fblog',
        },
        {
            path: '/login',
            component: () => import(/* webpackChunkName: "login" */ '../components/back_page/Login.vue'),
            meta: {title: '登录'}
        },
        {
            path: '/fregistered',
            component: () => import(/* webpackChunkName: "dashboard" */ '../components/front_page/FRegistered.vue'),
            meta: {title: '注册'}
        },
        {
            path: '/fretrieve',
            component: () => import(/* webpackChunkName: "dashboard" */ '../components/front_page/FRetrieve.vue'),
            meta: {
                title: '忘记密码',

            }
        },
        {
            path: '/fhome',
            component: FrontHome,
            meta: {title: '博客网站首页'},
            children: [
                {
                    path: '/fblog',
                    component: () => import('../components/front_page/FBlogs.vue'),
                    meta: {title: '首页'}
                },
                {
                    path: '/fblog/:blogId',
                    name:'FBlogDetail',
                    component: () => import( '../components/front_page/FBlogDetail.vue'),
                    meta: {title: '博客详情'}
                },
                {
                    path: '/about',
                    name:'About',
                    component: () => import('../components/front_page/About.vue'),
                    meta: {title: '关于我'}
                },
                {
                    path: '/link',
                    name:'Link',
                    component: () => import('../components/front_page/Link.vue'),
                    meta: {title: '关于我'}
                },
                {
                    path: '/ftype',
                    component: () => import('../components/front_page/FBlogType.vue'),
                    meta: {title: '博客查询页'}
                },
                {
                    path: '/farchive',
                    component: () => import('../components/front_page/FArchive.vue'),
                    meta: {title: '归档'}
                },
                {
                    path: '/fwall',
                    component: () => import('../components/front_page/FWall.vue'),
                    meta: {title: '留言墙'}
                },

            ]
        },
        {
            path: '/FUserHome',
            component: () => import('../components/front_page/FUserHome.vue'),
            meta: {title: '用户管理'},
            children: [
                {
                    path: '/user',
                    component: () => import('../components/front_page/FUser.vue'),
                    meta: {title: '个人资料',
                        requireAuth:true}
                },
                {
                    path: '/personal',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/front_page/FUserPwd.vue'),
                    meta: {title: '修改密码',
                        requireAuth:true}
                },
                {
                    path: '/comment',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/front_page/FUserComment.vue'),
                    meta: {title: '评论管理',
                        requireAuth:true}
                },

            ]
        },
        {
            path: '/back',
            component: () => import('../components/back_common/Home.vue'),
            meta: {title: '自述文件'},
            children: [
                {
                    path: '/dashboard',
                    component: () => import( '../components/back_page/Dashboard.vue'),
                    meta: {title: '后台系统首页',
                        requireAuth:true}
                },
                {
                    path: '/icon',
                    component: () => import( '../components/back_page/Icon.vue'),
                    meta: {title: '自定义图标',
                        requireAuth:true}
                },
                {
                    path: '/blogType',
                    component: () => import( '../components/back_page/BlogType.vue'),
                    meta: {title: '博客类别',
                        requireAuth:true}
                },
                {
                    path: '/blogs',
                    component: () => import( '../components/back_page/Blogs.vue'),
                    meta: {title: '所有博客',
                        requireAuth:true},
                },
                {
                    path: '/drafts',
                    component: () => import( '../components/back_page/DraftBlogs.vue'),
                    meta: {title: '草稿',
                        requireAuth:true},
                },
                {
                    path: '/recycle',
                    component: () => import( '../components/back_page/RecycleBlogs.vue'),
                    meta: {title: '回收站',
                        requireAuth:true},
                },
                {
                    path: '/blogEdit',
                    component: () => import( '../components/back_page/BlogEdit'),
                    meta: {title: '博客编辑',
                       }
                },
                {
                    path: '/blog/edit/:blogId',
                    component:() => import( '../components/back_page/BlogEdit'),
                    name: 'BlogEdit',
                    meta: {title: '编辑博客',
                        requireAuth:true}
                },
                {
                    path: '/comments',
                    component: () => import('../components/back_page/CommentData.vue'),
                    meta: {title: '评论管理',
                        requireAuth:true},
                },
                {
                    path: '/settings',
                    component: () => import('../components/back_page/UserSetting.vue'),
                    meta: {title: '用户设置',
                        requireAuth:true},
                },
                {
                    path: '/reduce',
                    component: () => import('../components/back_page/UserReduce.vue'),
                    meta: {title: '还原用户',
                        requireAuth:true},
                },
                {
                    path: '/links',
                    component: () => import('../components/back_page/LinkManage.vue'),
                    meta: {title: '友链管理',
                        requireAuth:true},
                },
                {
                    path: '/tabs',
                    component: () => import(/* webpackChunkName: "tabs" */ '../components/back_page/Tabs.vue'),
                    meta: {title: 'tab选项卡'}
                },

                {
                    // 权限页面
                    path: '/permission',
                    component: () => import(/* webpackChunkName: "permission" */ '../components/back_page/Permission.vue'),
                    meta: {title: '权限测试', permission: true}
                },
                {
                    path: '/404',
                    component: () => import(/* webpackChunkName: "404" */ '../components/back_page/404.vue'),
                    meta: {title: '404'}
                },
                {
                    path: '/403',
                    component: () => import(/* webpackChunkName: "403" */ '../components/back_page/403.vue'),
                    meta: {title: '403'}
                },
            ]
        },

        {
            path: '*',
            redirect: '/fblog'
        }
    ],

});

export default router

