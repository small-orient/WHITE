const path = require('path');

function resolve(dir) {
    return path.join(__dirname, '.', dir);
}

module.exports = {
   /* baseUrl: './',
    publicPath:'/',
    assetsDir: 'static',
    productionSourceMap: false,
    开发配置
    */
    publicPath:process.env.NODE_ENV === "production" ? "./" : "./",
    assetsDir: 'static',
    devServer: {
        open: false,// 自动打开浏览器
        host: 'localhost',
        port: 8080,
        proxy: { // 配置跨域
            '/': {
                target: 'https://www.blog.baidetu.cn/', // 这是后端地址
                /*target: 'http://localhost:8081/', // 这是后端地址*/
                ws: true,
                changOrigin: true,
                pathRewrite: {
                    '^/': ''
                }
            }
        }
    },
    //配置兼容IE
/*    chainWebpack: config => {
        config.module.rule('compile')
            .test(/\.js$/)
            .include
            .add(resolve('src'))
            .add(resolve('test'))
            .add(resolve('node_modules/webpack-dev-server/client'))
            .add(resolve('node_modules'))
            .end()
            .use('babel')
            .loader('babel-loader')
            .options({
                presets: [
                    ['@babel/preset-env', {
                        modules: false
                    }]
                ]
            });
    },*/
    transpileDependencies:[
      'element-ui','highlight.js','markdown-it','mavon-editor'
    ],

   /* //跨域访问图片
    devServer: {
        open: false,// 自动打开浏览器
        host: 'localhost',
        port: 8080,
        https: false,
        hotOnly: false,
        disableHostCheck: true,//Invalid Host header
        proxy: { // 配置跨域
            '/': {
                target: 'http://localhost:8081', // 这是后端地址
                ws: true,
                changOrigin: true,
                pathRewrite: {
                    '^/': '/'
                }
            }
        },
        before: app => { }
    }*/
}