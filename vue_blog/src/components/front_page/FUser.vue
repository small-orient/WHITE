<template>
    <div class="content-f-user">
        <!--头部导航栏-->
        <div style="margin: 20px 3%">
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item class="el-icon-user-solid" style="color: white" :to="{ path: '/user' }">
                    <span ><i style="color: rgb(234,236,241)">个人中心</i></span></el-breadcrumb-item>
                <el-breadcrumb-item ><span style="color: rgba(201,198,198,0.5)">
                    <i style="color: rgb(234,236,241)">个人资料</i></span></el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <el-row class="tac">
            <el-col :span="24">
                <div class="pwd-form">
                    <el-avatar style="margin-left: 160px;margin-top: 20px" :size="60"
                            :src="userAvatar"></el-avatar>
                    <div>
                        <el-button size="mini"
                                   style="background-color: rgba(201,198,198,0.5);
                               color: #f8faf9;margin-left: 150px;"
                                   @click="add()">
                            <input id="categoryPic"
                                 accept="image/*"
                                 type="file"
                                 name="image"
                                 ref="file"
                                 style="display: none"
                                 @change="getFile($event)" />
                            更换头像</el-button>
                    </div>
                </div>
                <!--用户信息表单-->
                <el-form :model="owner" :rules="rules"
                         ref="owner" label-width="0px"
                         class="pwd-form animated fadeInRight">
                    <el-form-item prop="nickName" style="margin-bottom: 40px;margin-top: 40px">
                        <el-button size="mini" style="background-color: rgba(201,198,198,0.5);color: #f8faf9"
                        >昵称</el-button><el-input
                            class="input-ph"
                            v-model="owner.nickName"
                            :placeholder="owner.nickName"></el-input>
                    </el-form-item>
                    <div class="dividing-f-pwd"></div>
                    <el-form-item prop="phone" style="margin-bottom: 40px;margin-top: 40px">
                        <el-button size="mini" style="background-color: rgba(201,198,198,0.5);color: #f8faf9"
                        >手机</el-button><el-input
                            class="input-ph"
                            v-model="owner.phone"
                            :placeholder="owner.phone"></el-input>
                    </el-form-item>
                    <div class="dividing-f-pwd"></div>
                    <el-form-item prop="email" style="margin-bottom: 40px;margin-top: 40px">
                        <el-button size="mini" style="background-color: rgba(201,198,198,0.5);color: #f8faf9"
                        >邮箱</el-button><el-input
                            class="input-np"
                            v-model="owner.email"
                            :placeholder="owner.email"></el-input>
                    </el-form-item>
                    <div class="dividing-f-pwd"></div>
                    <el-form-item prop="sign" style="margin-bottom: 40px;margin-top: 40px">
                        <el-button size="mini" style="background-color: rgba(201,198,198,0.5);color: #f8faf9"
                        >个人简介</el-button><el-input
                            type="textarea"
                            :rows="5"
                            style="width: 250px;padding-left: 18px"
                            :placeholder="owner.sign"
                            v-model="owner.sign">
                    </el-input>
                    </el-form-item>
                    <div class="dividing-f-pwd"></div>
                    <el-form-item size="large">
                        <el-button
                                style="margin-top: 40px;border-radius: 40px;margin-bottom: 300px"
                                type="success"
                                @click="submitUser('owner')"
                        >确认修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col></el-row>
    </div>

</template>

<script>
    //校验手机号
    const validPhoneUser=(rule, value,callback)=>{
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
    /*个人资料*/
    export default {
        name: "FUser",
        data(){
            return{
                owner: {
                    id:0,
                    nickName:'',
                    email:'',
                    phone:'',
                    sign:'',
                },
                realPhone:'', //真实手机号
                rules:{
                    nickName: [{trigger: 'blur'}],
                    sign: [{trigger: 'blur'}],
                    email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }],
                    phone: [{ trigger: 'blur',validator:validPhoneUser}],
                },

                //头像页面显示地址
                userAvatar:'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
                file:null,//头像对象

            }
        },
        methods:{
            //上传头像
            getFile: function (event) {
                this.file = event.target.files[0];
               /* alert(this.file.name);*/
                const isPic = this.file.type.indexOf('image') >= 0;
                const isLt2M = this.file.size / 1024 / 1024 < 2;
                if (!isPic) {
                    this.$message.error('只能为图片格式!');
                }else if (!isLt2M) {
                    this.$message.error('上传头像大小不能超过 2MB!');
                }else {
                    //存至数据库
                    let g_this = this;
                    const formData = new FormData()
                    formData.append('file', g_this.file)
                    this.axios.post("/user/avatar",formData,{
                        headers:{
                            "Authorization":localStorage.getItem("token"),
                            'Content-Type': 'multipart/form-data'
                        }
                    }).then((res) => {
                        console.log("文件上传返回：上传图片成功"+res)
                        //图片显示的路径
                        g_this.userAvatar="https://www.blog.baidetu.cn/"+res.data.data;
                        //刷新页面
                        g_this.$router.push('/user');
                        this.$message.success('头像已更换');
                    }).catch(error => {
                        this.$message.error("文件上传异常:图片上传失败"+error)
                    })
                }

            },
            //代替input方法打开头像上传栏（根据ref=file）
            add() {
                this.$refs.file.click();
            },
            //修改用户信息
            submitUser(owner){
                const g_this = this;
                g_this.owner.id = g_this.$store.getters.getUser.id;
                if (this.owner.email === '' && this.owner.phone === '') {
                    //手机与邮箱为空，则直接修改
                    g_this.axios.post('/user/owner/personal', g_this.owner,{
                        headers: {
                            "Authorization": localStorage.getItem("token")
                        }
                    }).then(res => {
                            //刷新页面
                            g_this.$router.push('/user');
                            g_this.$message.success('修改成功');
                        });
                } else if (this.owner.phone === '') {
                    //手机为空，只校验邮箱
                    this.$refs[owner].validateField('email', email_check => {
                        //邮箱未通过，false
                        if (email_check) {
                            return false;
                        }
                        //通过
                        g_this.axios.post('/user/owner/personal', g_this.owner,{
                            headers: {
                                "Authorization": localStorage.getItem("token")
                            }
                        }).then(res => {
                            //刷新页面
                            g_this.$router.push('/user');
                            g_this.$message.success('修改成功');
                            });
                    })
                } else if (this.owner.email === '') {
                    //邮箱为空
                    this.$refs[owner].validateField('phone', phone_check => {
                        //手机未通过，false
                        if (phone_check) {
                            return false;
                        }
                        //通过
                        g_this.axios.post('/user/owner/personal', g_this.owner,{
                            headers: {
                                "Authorization": localStorage.getItem("token")
                            }
                        }).then(res => {
                            //刷新页面
                            g_this.$router.push('/user');
                            g_this.$message.success('修改成功');
                        });
                    })
                }else if (this.owner.email !== '' && this.owner.phone !== ''){
                    //手机与邮箱都输入,全部校验
                    this.$refs[owner].validate((valid) => {
                        if (valid) {
                            //通过
                            g_this.axios.post('/user/owner/personal', g_this.owner,{
                                headers: {
                                    "Authorization": localStorage.getItem("token")
                                }
                            }).then(res => {
                                //刷新页面
                                g_this.$router.push('/user');
                                //再次隐藏手机号
                                g_this.owner.phone =g_this.hidePhone(g_this.realPhone)
                                g_this.$message.success('修改成功,刷新一下');
                                });
                        } else {
                            this.$message.error('修改个人资料失败，请重试');
                            return false;
                        }
                    });
                }else {
                    this.$message.error('出错了');
                    return false;
                }
            },
            //隐藏手机号中间4位数
            hidePhone(phone){
                const reg = /^(\d{3})\d{4}(\d{4})$/;
                return phone.replace(reg, "$1****$2");
            },
            //获取用户信息
            getOwner(){
                let g_this = this;
                g_this.axios.get("/user/owner",{
                    headers:{
                        "Authorization":localStorage.getItem("token")}
                }).then((res) => {
                    g_this.userAvatar =  res.data.data.avatar;
                    g_this.owner.nickName = res.data.data.nickName;
                    g_this.owner.sign = res.data.data.sign;
                    g_this.owner.email = res.data.data.email;
                    g_this.realPhone = res.data.data.phone; //存放真实手机号
                    g_this.owner.phone = g_this.hidePhone(res.data.data.phone);
                }).catch(error=>{
                    this.$message.error("获取用户信息异常")
                })
            },
        },
        created() {
            let g_this = this;
            g_this.getOwner();
        }

    }
</script>

<style scoped>
    @media screen and (max-width: 1280px){
        .content-f-user {
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
        .content-f-user {
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
        .content-f-user {
            height: 100%;
            padding: 10px;
            box-sizing: border-box;

        }
        .pwd-form{
            margin-left: 25%
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