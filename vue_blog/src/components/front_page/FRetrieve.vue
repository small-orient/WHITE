<template>
    <div class="login-wrap">
        <div class="SiteName-re">
            <span class="iconfont">
                <h3 >WHITE'S
                <span class="icon-control-re iconfont icon-wenzhang_huaban" ></span>
                </h3>
            </span>

        </div>
        <div class="ms-login">
            <el-form   class="ms-content">
                <div class="ms-title">
                    <span  style="color: black;font-weight: bold;font-size: 30px">找回密码</span><br>
                    <span style="color: rgba(11,10,10,0.40);font-size: 15px">请填写您要找回的账号信息</span>
                </div>
                <template>
                    <div class="">
                        <el-row class="tac">
                            <el-col :span="24">
                                <!--验证手机号表单-->
                                <el-form :model="phone"
                                         :rules="rulesPhone"
                                         ref="phone"
                                         label-width="0px"
                                         class="pwd-form animated fadeInRight"
                                         v-show="verifyPhone"
                                >
                                    <el-form-item prop="phone" style="margin-bottom: 40px;margin-top: 40px">
                                        <el-button size="mini" style="background-color: rgba(241,134,20,0.5);color: #f8faf9"
                                        >手机号 <i class="el-icon-right"></i></el-button><el-input
                                            class="input-ph"
                                            v-model="phone.phone"
                                            placeholder="请填写你的手机号"></el-input>
                                    </el-form-item>
                                    <div class="dividing-f-pwd"></div>
                                    <el-form-item prop="verification" style="margin-bottom: 40px;margin-top: 40px">
                                        <el-button size="mini" style="background-color: rgba(241,134,20,0.5);color: #f8faf9"
                                        >验证码 <i class="el-icon-right"></i></el-button><el-input
                                            class="input-ve"
                                            v-model="phone.verification"
                                            placeholder="请填写验证码"></el-input>
                                        <el-button
                                                style="border-radius: 30px;margin-left:25px;"
                                                @click="getVerification('param')"
                                                type="primary"
                                                v-show="codeBtn"
                                        >获取验证码</el-button>
                                        <el-button
                                                style="border-radius: 30px;margin-left:25px;"
                                                type="primary"
                                                icon="el-icon-loading"
                                                disabled
                                                v-show="!codeBtn"
                                        ></el-button>
                                    </el-form-item>
                                    <div class="dividing-f-pwd"></div>
                                    <el-form-item size="large">
                                        <el-button
                                                style="margin-top: 40px;
                                                border-radius: 40px;
                                                margin-left: 35%"
                                                type="success"
                                                @click="submitForm('phone')"
                                        >立即验证</el-button>
                                    </el-form-item>
                                </el-form>
                                <!--修改密码表单-->
                                <el-form :model="password" :rules="rulesPwd"
                                         ref="password" label-width="0px"
                                         v-show="updatePwd"
                                         class="pwd-form animated fadeInRight">
                                    <el-form-item prop="newPassword" style="margin-bottom: 40px;margin-top: 40px">
                                        <el-button size="mini" style="background-color: rgba(201,198,198,0.5);color: #f8faf9"
                                        >新密码</el-button><el-input
                                            type="password"
                                            class="input-np"
                                            v-model="password.newPassword"
                                            placeholder="请设置密码"></el-input>
                                    </el-form-item>
                                    <div class="dividing-f-pwd"></div>
                                    <el-form-item prop="confirmPwd" style="margin-bottom: 40px;margin-top: 40px">
                                        <el-button size="mini" style="background-color: rgba(201,198,198,0.5);color: #f8faf9"
                                        >确认密码</el-button><el-input
                                            type="password"
                                            class="input-cf"
                                            v-model="password.confirmPwd"
                                            placeholder="请确认密码"></el-input>
                                    </el-form-item>
                                    <el-form-item size="large">
                                        <el-button
                                                style="margin-top: 40px;
                                                border-radius: 40px;
                                                margin-left: 135px"
                                                type="success"
                                                @click="submitFormPassword('password')"
                                        >确认修改</el-button>
                                    </el-form-item>
                                </el-form>
                            </el-col>
                        </el-row>
                    </div>
                </template>
                <el-link class="el-icon-back"
                         style="font-size: 15px;margin-left: 80%;color: cornflowerblue"
                         @click="goHome"
                >返回首页</el-link>
            </el-form>
        </div>
    </div>
</template>

<script>
    /**
     * 找回密码
     */

    //校验手机号
    const validPhone=(rule, value,callback)=>{
        const reg = /^[1][3-9][0-9]{9}$/;
        let g_this = this;
        if (value === '' || value === undefined) {
            callback(new Error('请输入手机号码！'))
        } else {
            if ((!reg.test(value)) && value !== '') {
                callback(new Error('请输入正确的手机号码！'))
            } else {
                //校验成功
                callback();
            }
        }
    }

    //校验密码：8-16位数字、字母和特殊字符~!@#$%^&*-+=_.
    const validPwd=(rule, value,callback)=>{
        const reg = /^[~!@#$%^&*\-+=_.0-9a-zA-Z]{8,20}$/;
        if (value === '' || value === undefined) {
            callback(new Error('请输入密码'))
        } else {
            if ((!reg.test(value)) && value !== '') {
                callback(new Error('请输入8-20位数字、字母、或特殊字符'),{duration: 3 * 1000})
            } else {
                //校验成功
                callback();
            }
        }
    }
    export default {
        name: "FRetrieve",

        data: function () {
            //验证新密码：注意，需放在data下声明变量，才能获取到newPassword
            const validNewPwd=(rule, value,callback)=>{
                if (value === '' || value === undefined) {
                    callback(new Error('请确认密码'))
                } else if(!(value === this.password.newPassword)) {
                    callback(new Error('两次输入的密码不一致'))
                }else {
                    callback();
                }
            }
            return {
                updatePwd:false,//修改密码表单显示
                verifyPhone:true,//验证手机号表单显示
                codeBtn:true,//验证手机号表单显示
                /*验证手机表单*/
                phone: {
                    verification: '',
                    phone:'',
                },
                rulesPhone: {
                    phone: [{required: true, trigger: 'blur',validator:validPhone}],
                },

                /*修改密码表单*/
                password: {
                    newPassword:'',
                    confirmPwd:'',
                },
                rulesPwd: {
                    newPassword:[{required: true, trigger: 'blur',validator:validPwd}],
                    confirmPwd: [{required: true,trigger: 'blur',validator:validNewPwd}],
                },

                //储存至后端对象
                user:{
                    userId:0,
                    phone:'',
                    smsCode:'',
                    newPassword:'',
                }
            };
        },
        methods: {
            //发送手机验证码
            getVerification() {
                const g_this = this;
                const reg = /^[1][3-9][0-9]{9}$/;
                if (g_this.phone.phone === '' || g_this.phone.phone === undefined || !reg.test(g_this.phone.phone)) {
                    g_this.$message.error('请填写正确的手机号');
                } else {
                    console.log(g_this.phone.phone + "---------phone")
                    //60s内不允许再次点击
                    this.codeBtn = false;
                    //后台修改
                    this.axios.post('/sms/send', g_this.phone.phone, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }).then(res => {
                        g_this.$message.success('验证码已发送，请注意查收，1分钟内请勿再次获取');
                        setTimeout(() => {
                            this.codeBtn = true;
                        }, 1000 * 60);
                    })
                }
            },
            //验证手机表单
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const g_this = this;
                        g_this.user.phone = g_this.phone.phone;
                        g_this.user.smsCode = g_this.phone.verification;
                        this.axios.post('/sms/retrieve', g_this.user).then(res => {
                                if (res.data.data.userId > 0){
                                    g_this.user = res.data.data;
                                    //验证成功显示修改密码框
                                    g_this.verifyPhone = false;
                                    g_this.updatePwd = true;
                                }else {
                                    this.$message.error('校验失败');
                                }
                        })
                    } else {
                        this.$message.error('请输入正确的信息！');
                        console.log('error submit!!');
                        return false;
                    }
                });
            },

            //修改密码表单
            submitFormPassword(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        //1.保存全局this
                        const g_this = this;
                        g_this.user.newPassword = g_this.password.confirmPwd;
                        //调用找回密码接口
                        this.axios.post('/sms/retrievePwd',g_this.user,{
                            headers:{
                                "Authorization":localStorage.getItem("token")
                            }
                        }).then(res => {
                            //成功
                            g_this.$message.success('修改成功，即将跳转至登录页面');
                            //前端清除用户信息
                            g_this.$store.commit("REMOVE_INFO");
                            setTimeout(() =>{
                                g_this.$router.push("/login");
                            },2000);
                        });

                    } else {
                        this.$message.error('修改失败，请重试');
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            //返回首页
            goHome(){
                this.$router.push("/fblog");
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
        padding: 30px 30px;
    }


    .login-btn button {
        width: 100%;
        height: 36px;
        margin-bottom: 10px;
    }

    /**
    表单css
     */
    .pwd-form{
        margin-left: 7%
    }

    .input-ph{
        width: 250px;
        margin-left: 40px;
    }
    .input-ve{
        width: 130px;
        margin-left: 40px;
    }
    .input-np{
        width: 250px;
        margin-left: 40px;
    }
    .input-cf{
        width: 250px;
        margin-left:25px;
    }
</style>



