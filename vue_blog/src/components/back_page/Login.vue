<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">
                <span style="margin-left: 38%">white's</span>
                <el-link class="el-icon-back"
                         style="font-size: 10px;margin-left: 12%"
                         @click="goHome"
                >返回首页</el-link>
            </div>
            <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <!--用户名-->
                <el-form-item prop="userName">
                    <el-input v-model="param.userName" placeholder="请输入账号">
                        <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                    </el-input>
                </el-form-item>
                <!--密码-->
                <el-form-item prop="password">
                    <el-input
                            type="password"
                            placeholder="请输入密码"
                            v-model="param.password"
                            @keyup.enter.native="submitForm()"
                    >
                        <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                    </el-input>
                </el-form-item>
                <!--登录按钮-->
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()">登录</el-button>
                </div>
                <p class="login-tips">
                    <el-link
                        @click="Ffretrieve"
                        style="color: #419fff">忘记密码？
                    </el-link>
                    <el-link
                            @click="Ffregistered"

                            style="color: #419fff;margin-left: 56%">立即注册
                    </el-link>
                </p>
            </el-form>
        </div>
    </div>
</template>

<script>
    //校验用户名：只允许5-20位字母或数字
    const validName=(rule, value,callback)=>{
        const reg =   /^[0-9a-zA-Z]{5,16}$/;
        if (value === '' || value === undefined) {
            callback(new Error('请输入用户名'))
        } else {
            if ((!reg.test(value)) && value !== '') {
                callback(new Error('用户名为5-16位字母或数字组成'),{duration: 3 * 1000})
            } else {
                //校验成功
                callback();
            }
        }
    }
    export default {
        data: function () {
            return {
                param: {
                    userName: '',
                    password: '',
                },
                rules: {
                    userName: [{required: true,trigger: 'blur',validator:validName}],
                    password: [{required: true, message: '请输入密码', trigger: 'blur'}],
                },
            };
        },
        methods: {
            //返回首页
            goHome(){
                this.$router.push("/fblog");
            },
            //找回密码
            Ffretrieve(){
                this.$router.push("/fretrieve");
            },
            //注册
            Ffregistered(){
                this.$router.push("/fregistered");
            },
            submitForm() {
                this.$refs.login.validate(valid => {
                    if (valid) {
                        console.log("valid--用户登录"+valid)
                        //1.保存全局this
                        const g_this = this;
                        this.axios.post('/user/login', this.param)
                            .then(res => {
                                /*2.查看数据是否返回
                                console.log(res.headers);
                                console.log(res);
                                */
                                //3.获取token
                                const jwt = res.headers['authorization'];
                                //4.用户信息：后台封装的Result.data获取
                                const userInfo = res.data.data;
                                //5.将token与用户信息保存至浏览器
                                g_this.$store.commit("SET_TOKEN", jwt);
                                g_this.$store.commit("SET_USERINFO", userInfo);

                                /*6.测试从浏览器获取用户信息：
                                *console.log(g_this.$store.getters.getUser)
                                * */

                                //7.判断用户是否为博主
                                if (g_this.param.userName === 'baidetu'){
                                    //是，跳转至仪表盘首页
                                    g_this.$router.push('/dashboard');
                                    this.$message.success('登录成功');
                                }else {
                                    //否，跳转至博客首页
                                    g_this.$router.push('/fblog');
                                    this.$message.success('登录成功');
                                }

                            });

                    } else {
                        this.$message.error('请输入账号和密码');
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
        },
    };

    /*
    这里可以做一个路由跳转方法，点击一个按钮，调用一个方法，然后用 this.$router.push('/跳转路径')跳转
    submitForm() {
        this.$refs.login.validate(valid => {
            if (valid) {
                this.$message.success('登录成功');
                localStorage.setItem('ms_username', this.param.username);
                this.$router.push('/');
            } else {
                this.$message.error('请输入账号和密码');
                console.log('error submit!!');
                return false;
            }
        });
    }*/
</script>

<style scoped>
    /*背景图片设置*/
    .login-wrap {
        position: relative;
        width: 100%;
        height: 100%;
        background-image: url(../../assets/img/login-bg.jpg);
        background-size: 100%;
    }

    .ms-title {
        width: 100%;
        line-height: 50px;
        font-size: 20px;
        color: #fff;
        border-bottom: 1px solid #ddd;
    }

    .ms-login {
        position: absolute;
        left: 50%;
        top: 50%;
        width: 350px;
        margin: -190px 0 0 -175px;
        border-radius: 5px;
        background: rgba(255, 255, 255, 0.4);
        overflow: hidden;
    }

    .ms-content {
        padding: 30px 30px;
    }

    .login-btn {
        text-align: center;
    }

    .login-btn button {
        width: 100%;
        height: 36px;
        margin-bottom: 10px;
    }

    .login-tips {
        font-size: 12px;
        line-height: 30px;
        color: #fff;
    }
</style>