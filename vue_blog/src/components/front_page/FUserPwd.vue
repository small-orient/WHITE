<template>
    <div class="content-f-pwd">
        <!--头部导航栏-->
        <div style="margin: 20px 3%">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item class="el-icon-user-solid" style="color: white" :to="{ path: '/user' }">
                    <span ><i style="color: rgb(234,236,241)">个人中心</i></span></el-breadcrumb-item>
                <el-breadcrumb-item >
                     <i style="color: rgb(234,236,241)">修改密码</i></el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-row class="tac">
            <el-col :span="24">
            <el-form :model="param" :rules="rules"
                     ref="param" label-width="0px"
                     class="pwd-form animated fadeInRight">
            <el-form-item prop="phone" style="margin-bottom: 40px;margin-top: 40px">
                <el-button size="mini" style="background-color: rgba(201,198,198,0.5);color: #f8faf9"
                >手机号</el-button><el-input
                    class="input-ph"
                    v-model="param.phone"
                    placeholder="请填写你的手机号"></el-input>
            </el-form-item>
                <div class="dividing-f-pwd"></div>
                <el-form-item prop="verification" style="margin-bottom: 40px;margin-top: 40px">
                    <el-button size="mini" style="background-color: rgba(201,198,198,0.5);color: #f8faf9"
                    >验证码</el-button><el-input
                        class="input-ve"
                        v-model="param.verification"
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
                <el-form-item prop="newPassword" style="margin-bottom: 40px;margin-top: 40px">
                    <el-button size="mini" style="background-color: rgba(201,198,198,0.5);color: #f8faf9"
                    >新密码</el-button><el-input
                        type="password"
                        class="input-np"
                        v-model="param.newPassword"
                        placeholder="请设置密码"></el-input>
                </el-form-item>
                <div class="dividing-f-pwd"></div>
                <el-form-item prop="confirmPwd" style="margin-bottom: 40px;margin-top: 40px">
                    <el-button size="mini" style="background-color: rgba(201,198,198,0.5);color: #f8faf9"
                    >确认密码</el-button><el-input
                        type="password"
                        class="input-cf"
                        v-model="param.confirmPwd"
                        placeholder="请确认密码"></el-input>
                </el-form-item>
                <div class="dividing-f-pwd"></div>
                <el-form-item size="large">
                    <el-button
                            style="margin-top: 40px;border-radius: 40px;margin-bottom: 300px"
                            type="success"
                            @click="submitForm('param')"
                    >确认修改</el-button>
                </el-form-item>
            </el-form>
        </el-col></el-row>
    </div>
</template>

<script>

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
    //校验手机号
    const validPhone=(rule, value,callback)=>{
        const reg = /^[1][3-9][0-9]{9}$/;
        if (value === '' || value === undefined) {
            callback(new Error('请输入手机号码'))
        } else {
            if ((!reg.test(value)) && value !== '') {
                callback(new Error('请输入正确的手机号码'))
            } else {
                //校验成功
                callback();
            }
        }
    }

    export default {
        name: "FUserPwd",
        data() {
            //验证新密码：注意，需放在data下声明变量，才能获取到newPassword
            const validNewPwd=(rule, value,callback)=>{
                if (value === '' || value === undefined) {
                    callback(new Error('请确认密码'))
                } else if(!(value === this.param.newPassword)) {
                    callback(new Error('两次输入的密码不一致'))
                }else {
                    callback();
                }
            }

            return{
                codeBtn:true,//发送验证码按钮
                param: {
                verification:'',
                phone:'',
                newPassword:'',
                confirmPwd:'',
                },
                rules: {
                    verification: [{required: true, message: '请输入验证码', trigger: 'blur'}],
                    newPassword:[{required: true, trigger: 'blur',validator:validPwd}],
                    confirmPwd: [{required: true,trigger: 'blur',validator:validNewPwd}],
                    phone: [{required: true, trigger: 'blur',validator:validPhone}],
                },
                //储存至后端对象
                user:{
                    userId:0,
                    phone:'',
                    smsCode:'',
                    newPassword:'',
                }
            }
            },
        methods:{
            //发送手机验证码
            getVerification(){
                const g_this = this;
                const reg = /^[1][3-9][0-9]{9}$/;
                if (g_this.param.phone === ''|| g_this.param.phone ===undefined ||!reg.test(g_this.param.phone)){
                    g_this.$message.error('请填写正确的手机号');
                }else {
                    console.log(g_this.param.phone+"---------123")
                    //60s内不允许再次点击
                    this.codeBtn = false;
                    //后台修改
                    this.axios.post('/sms/send',g_this.param.phone,{
                        headers:{
                            'Content-Type':'application/json'
                        }
                    }).then(res => {
                            g_this.$message.success('验证码已发送，请注意查收，1分钟内请勿再次获取');
                            setTimeout(() =>{
                                this.codeBtn = true;
                            },1000*60);
                    })
                }
            },

            //修改密码
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const g_this = this;
                        g_this.user.userId = g_this.$store.getters.getUser.id;
                        g_this.user.phone = g_this.param.phone;
                        g_this.user.smsCode = g_this.param.verification;
                        g_this.user.newPassword = g_this.param.confirmPwd;
                        //后台修改
                        this.axios.post('/sms/updatePwd',g_this.user,{
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
                        this.$message.error('请输入正确的信息！');
                        console.log('error submit!!');
                        return false;
                    }
                });
            },

            },

}
</script>

<style scoped>
    @media screen and (max-width: 1280px){
        .content-f-pwd {
            height: 100%;
            padding: 10px;
            box-sizing: border-box;
            margin-left: 10%;
        }
        .pwd-form{
            margin-left: 18%
        }
        .dividing-f-pwd {
            width: 70%;
            line-height: 50px;
            font-size: 20px;
            color: #fff;
            border-bottom: 1px solid #ddd;
        }

    }
    @media screen and (max-width: 960px){
        .content-f-pwd {
            height: 100%;
            padding: 10px;
            box-sizing: border-box;
            margin-left: 20%;
        }
        .pwd-form{
            margin-left: 0
        }
        .dividing-f-pwd {
            width: 365px;
            line-height: 50px;
            font-size: 20px;
            color: #fff;
            border-bottom: 1px solid #ddd;
        }

    }
    @media screen and (min-width: 1280px){
        .content-f-pwd {
            height: 100%;
            padding: 10px;
            box-sizing: border-box;

        }
        .pwd-form{
            margin-left: 20%
        }
        .dividing-f-pwd {
            width: 70%;
            line-height: 50px;
            font-size: 20px;
            color: #fff;
            border-bottom: 1px solid #ddd;
        }
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