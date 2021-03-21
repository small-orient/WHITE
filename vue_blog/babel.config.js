//兼容IE配置
module.exports = {
    presets: [
        [
            "@vue/app",
            {
                "useBuiltIns": "entry",
                polyfills: [
                    'es6.promise',
                    'es6.symbol'
                ]
            }
        ]
    ],
};
