# WHITE'S
springboot+vue个人博客

项目地址：https://www.baidetu.cn/#/fblog

## 介绍 
基于前后端分离，通过配置跨域将axios请求转发到后端。前端页面分为：back_page、front_page，back_page作为后台管理页面，front_page作为前台显示页面。其中front_page是我自己结合ElementUI编写，back_page使用了模板**vue-element-admin**，该模板对应地址：https://panjiachen.gitee.io/vue-element-admin/。

## 后端框架
1.springboot
2.MybatisPlus
3.shiro+JWT
4.redis
5.MySQL

## 前端框架
1.vue
2.axios
3.ElementUI
4.编辑器使用Markdown(mavon-editor)

## 快速运行  
  
一、后端:

`1.进入springboot目录-->找到sql目录下的springboot_vue.sql，在mysql数据库中建表;
2.springboot项目的application.properties中选择spring.profiles.active=dev，在开发环境中启动；
3.修改对应配置文件的mysql连接信息；
4.在IntelliJ IDEA中启动。`

访问测试：http://localhost:8081/blog/hot ，该接口使用get请求，访问成功代表项目启动成功。或者你也可以访问任意可访问的接口进行测试。

二、前端：
确保你的运行环境能启动vue，这里的vue版本推荐3以上。
`
  1.在vue_blog目录下安装依赖：npm install
  2.启动测试：npm run dev，http://localhost:8080
`
 也可以在IntelliJ IDEA中打开vue_blog，在IDEA的Terminal输入对应的命令即可。后续的开发也可以继续在IDEA中进行。
  
## 小结
因为一直想做一个网站，就是好奇网站怎么做的，所以驱使我完成了white's。后续会一直维护网站，期待变得更好~




