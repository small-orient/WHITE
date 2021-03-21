<template>
    <div class="login-wrap">
        <div class="SiteName-re">
            <span class="iconfont">
                <h3 >WHITE'S BLOG
                <span class="icon-control-re iconfont icon-wenzhang_huaban" ></span>
                </h3>
            </span>

        </div>
        <div class="ms-login">
            <el-form :model="param" :rules="rules"
                     status-icon
                     ref="registered" label-width="0px" class="ms-content">
                <div class="ms-title">
                    <span  style="color: black;font-weight: bold;font-size: 30px">欢迎注册</span>
                    <el-link class="el-icon-back"
                             style="font-size: 15px;margin-left: 53%"
                             @click="goHome"
                    >返回首页</el-link><br>
                    <span style="color: rgba(11,10,10,0.25);font-size: 15px">已有账号？</span>
                    <el-link @click="goLogin" style="color: cornflowerblue">登录</el-link>

                </div>
                <!--用户名-->
                <el-form-item prop="userName" style="margin-bottom: 40px">
                   用户名<el-input
                        class="input-re"
                        v-model="param.userName"
                        placeholder="请设置用户名(用户名为5-16位字母或数字)"></el-input>
                </el-form-item>
                <!--密码-->
                <el-form-item prop="password" style="margin-bottom: 40px">
                    密&nbsp;&nbsp;&nbsp;&nbsp;码<el-input
                            class="input-re-pwd"
                            type="password"
                            placeholder="请设置密码(8-20位数字、字母或特殊字符:~!@#$%^&*?)"
                            v-model="param.password"

                    ></el-input>
                </el-form-item>
                <!--昵称-->
                <el-form-item prop="nickName" style="margin-bottom: 40px">
                    昵&nbsp;&nbsp;&nbsp;&nbsp;称<el-input
                        class="input-re-pwd"
                        placeholder="请设置昵称(不设置将随机赋予)"
                        v-model="param.nickName"

                ></el-input>
                </el-form-item>
                <!--邮箱-->
                <el-form-item prop="email"  style="margin-bottom: 40px">
                    邮&nbsp;&nbsp;&nbsp;&nbsp;箱<el-input
                        class="input-re-em"
                        placeholder="请设置邮箱(可不填)"
                        v-model="param.email"
                ></el-input>
                </el-form-item>
                <!--手机号-->
                <el-form-item prop="phone" >
                    手机号<el-input
                        class="input-re"
                        placeholder="请添加手机号(可用于找回密码，可不填)"
                        v-model="param.phone"
                        @keyup.enter.native="submitForm"
                ></el-input>
                </el-form-item>
                <!--注册按钮-->
                <div class="login-btn">
                    <el-button type="primary"
                               style="border-radius: 30px" @click="submitForm('registered')">注册</el-button>
                </div>

            </el-form>
        </div>
    </div>
</template>

<script>
    //校验手机号
    const validPhone=(rule, value,callback)=>{
        const reg = /^[1][3-9][0-9]{9}$/;
        if (value === '' || value === undefined) {
            /*callback(new Error('请输入手机号码！'))*/
        } else {
            if ((!reg.test(value)) && value !== '') {
                callback(new Error('请输入正确的手机号码'))
            } else {
                //校验成功
                callback();
            }
        }
    }
    //校验密码：8-16位数字、字母和特殊字符~!@#$%^&*-+=_.
    const validPwd=(rule, value,callback)=>{
        const reg = /^[~!@#$%^&*?\-+=_.0-9a-zA-Z]{8,20}$/;
        if (value === '' || value === undefined) {
            callback(new Error('请输入密码'))
        } else {
            if ((!reg.test(value)) && value !== '') {
                callback(new Error('请输入8-20位数字、字母、或特殊字符(~!@#$%^&*?)'),{duration: 3 * 1000})
            } else {
                //校验成功
                callback();
            }
        }
    }

    //校验用户名：只允许5-20位字母或数字
    const validName=(rule, value,callback)=>{
        const reg =   /^[0-9a-zA-Z]{5,16}$/;
        if (value === '' || value === undefined) {
            callback(new Error('请输入用户名'))
        } else {
            if ((!reg.test(value)) && value !== '') {
                callback(new Error('用户名为5-16位字母或数字'),{duration: 3 * 1000})
            } else {
                //校验成功
                callback();
            }
        }
    }
    export default {
        name: "FRegistered",
        data: function () {
            return {
                param: {
                    userName: '',
                    password: '',
                    email:'',
                    phone:'',
                    nickName:'',
                },
                rules: {
                    userName: [{required: true, trigger: 'blur',validator:validName}],
                    password: [{required: true, trigger: 'blur',validator:validPwd}],
                    nickName: [{trigger: 'blur'}],
                    email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }],
                    phone: [{ trigger: 'blur',validator:validPhone}],
                },
            };
        },
        methods: {
            //跳转登录页面
            goLogin(){
                this.$router.push("/login");
            },
            //返回首页
            goHome(){
                this.$router.push("/fblog");
            },
            //提交表单
            submitForm(registered) {
                console.log("submitForm")
                if (this.param.email === '' && this.param.phone === '') {
                    //如果没输入手机号与邮箱
                    this.$refs[registered].validateField('userName', userName_check => {
                        //用户名未通过，false
                        if (userName_check) {
                            return false;
                        }
                        this.$refs[registered].validateField('password', password_check => {
                            //密码未通过，false
                            if (password_check)
                                return false;

                            //没设置昵称，随机给
                            if (!this.param.nickName){
                                this.param.nickName = this.getRandomName(6);
                            }

                            //通过
                            const g_this = this;
                            g_this.axios.post('/user/registered', g_this.param)
                                .then(res => {

                                    //2.跳转至登录页面
                                    g_this.$router.push('/login');
                                    g_this.$message.success('注册成功');
                                });

                        })
                    });
                } else if (this.param.phone === '') {
                    //如果没输入手机号
                    this.$refs[registered].validateField('userName', userName_check => {
                        //用户名未通过，false
                        if (userName_check) {
                            return false;
                        }
                        this.$refs[registered].validateField('password', password_check => {
                            //密码未通过，false
                            if (password_check) {
                                return false;
                            }

                            this.$refs[registered].validateField('email', email_check => {
                                //邮箱未通过，false
                                if (email_check) {
                                    return false;
                                }
                                //没设置昵称，随机给
                                if (!this.param.nickName){
                                    this.param.nickName = this.getRandomName(6);
                                }
                                //通过
                                const g_this = this;
                                g_this.axios.post('/user/registered', g_this.param)
                                    .then(res => {
                                        //2.跳转至登录页面
                                        g_this.$router.push('/login');
                                        g_this.$message.success('注册成功');

                                    });
                            })
                        })
                    })
                } else if (this.param.email === '') {
                    //没输入邮箱
                    this.$refs[registered].validateField('userName', userName_check => {
                        //用户名未通过，false
                        if (userName_check) {
                            return false;
                        }
                        this.$refs[registered].validateField('password', password_check => {
                            //密码未通过，false
                            if (password_check) {
                                return false;
                            }

                            this.$refs[registered].validateField('phone', phone_check => {
                                //邮箱未通过，false
                                if (phone_check) {
                                    return false;
                                }
                                //没设置昵称，随机给
                                if (!this.param.nickName){
                                    this.param.nickName = this.getRandomName(6);
                                }
                                //通过
                                const g_this = this;
                                g_this.axios.post('/user/registered', g_this.param)
                                    .then(res => {
                                        //2.跳转至登录页面
                                        g_this.$router.push('/login');
                                        g_this.$message.success('注册成功');

                                    });
                                  })
                                })
                            })
                  } else {
                    //全部输入
                    this.$refs[registered].validate((valid) => {
                        if (valid) {

                            //没设置昵称，随机给
                            if (!this.param.nickName){
                                this.param.nickName = this.getRandomName(6);
                            }
                            //通过
                            const g_this = this;
                            g_this.axios.post('/user/registered', g_this.param)
                                .then(res => {

                                    //2.跳转至登录页面
                                    g_this.$router.push('/login');
                                    g_this.$message.success('注册成功');

                                });
                        } else {
                            this.$message.error('注册失败，请重试');
                            console.log('error submit!!');
                            return false;
                        }
                    });

                }

            },
            //随机生成中文范围内的Unicode码：Unicode中文范围:4e00,9fa5）-16进制范围
            randomAccess(min,max){
                return Math.floor(Math.random() * (min - max) + max)
            },
            //将该码转成中文
            decodeUnicode(str) {
                //Unicode显示方式是\u4e00
                str = "\\u"+str
                str = str.replace(/\\/g, "%");
                //转换中文
                str = unescape(str);
                //将其他受影响的转换回原来
                str = str.replace(/%/g, "\\");
                return str;
            },
            //获取英文数字
            getRandomName(NameLength){
                let name = ""
                for(let i = 0;i<NameLength;i++){
                    let unicodeNum  = ""
                   /* unicodeNum = this.randomAccess(0x4e00,0x9fa5).toString(16) 中文*/
                    //获取英文数字
                    unicodeNum = this.randomAccess(0x30,0x7a).toString(16)
                    name += this.decodeUnicode(unicodeNum)
                }
                return name
            }

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

    @media screen and (max-width: 1080px){
        .SiteName-re{
           margin-left: 5%;
            margin-top: 5%;
        }
        .iconfont{
            font-size: 20px;
        }
        .icon-control-re{
            font-size: 30px;
        }
    }

    @media screen and (min-width: 1080px){
        .SiteName-re{
            margin-left: 5%;
            margin-top: 5%;
        }
        .iconfont{
            font-size: 40px;
        }
        .icon-control-re{
            font-size: 60px;
        }
    }
    /*背景图片设置*/
    .login-wrap {
        position: relative;
        width: 100%;
        height: 100%;
        background-image: url(../../assets/img/login-bg.jpg);
        background-size: 100%;
        overflow: auto;
    }

    .ms-title {
        width: 100%;
        line-height: 25px;
        text-align: left;
        font-size: 20px;
        color: #fff;
        margin-bottom: 60px;
        margin-top: 25px;
    }

    .ms-login {
        position: absolute;
        left: 50%;
        top: 50%;
        width: 480px;
        margin: -300px 0 0 -240px;
        border-radius: 5px;
        background: rgba(255, 255, 255, 0.8);
        overflow: hidden;
    }

    .ms-content {
        padding: 20px 30px;
    }
    .input-re{
        width: 362px;
        margin-left: 15px;
    }
    .input-re-pwd{
        width: 362px;
        margin-left: 13px;
    }
    .input-re-em{
        width: 362px;
        margin-left: 13px;
    }

    .login-btn {
        text-align: center;
        margin-top: 50px;

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



