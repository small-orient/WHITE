<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-calendar"></i> 写博客
                </el-breadcrumb-item>
                <el-breadcrumb-item>编辑</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="my-content">
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                    <el-form-item label="标题" prop="title">
                        <el-input v-model="ruleForm.title">
                            <!--原创转载声明-->
                            <el-select
                                    slot="prepend"
                                    v-model="ruleForm.createSign"
                                    style="width: 90px"
                                    placeholder="请选择">
                                <el-option
                                        v-for="(item,index) in createSignValue"
                                        :key="index"
                                        :label="item"
                                        :value="item">
                                </el-option>
                            </el-select>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="摘要" prop="summary" >
                        <el-input type="textarea"
                                  placeholder="请输入摘要..."
                                  v-model="ruleForm.summary"></el-input>
                    </el-form-item>
                    <el-form-item label="内容" prop="content">
                       <mavon-editor v-model="ruleForm.content"></mavon-editor>
                    </el-form-item>
                    <el-form-item label="分类" prop="typeName" >
                        <el-input v-model="ruleForm.typeName" placeholder="请输入博客类型" ></el-input>
                    </el-form-item>
                    <el-form-item label="标签" prop="keyword" >
                        <el-select
                                v-model="ruleForm.keyword"
                                multiple
                                filterable
                                allow-create
                                default-first-option
                                placeholder="请选择标签"
                                style="margin-right: 85%;width: 300px"
                        >
                            <el-option
                                    v-for="(item,index) in keywordValue"
                                    :key="index"
                                    :label="item"
                                    :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <!--博客封面图-->
                    <el-form-item label="上传图片" prop="imageUrl">
                        <el-upload
                                class="avatar-uploader"
                                action="string"
                                list-type="picture-card"
                                :on-preview="handlePictureCardPreview"
                                :http-request="uploadImage"
                                :before-upload="beforeAvatarUpload"
                                :on-remove="handleRemove"
                        >
                            <img v-if="this.imgView" :src="this.imgView" class="avatar">
                            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                        </el-upload>
                        <el-dialog :visible.sync="dialogVisible">
                            <img width="100%" :src="this.dialogImageUrl" alt="">
                        </el-dialog>
                    </el-form-item>

                    <!--各个状态开启情况-->
                    <el-form-item >
                    <el-checkbox-group v-model="checkList">
                        <el-checkbox :label="checkRecommend"></el-checkbox>
                        <el-checkbox :label="checkShare"></el-checkbox>
                        <el-checkbox :label="checkPraise"></el-checkbox>
                        <el-checkbox :label="checkComment"></el-checkbox>
                    </el-checkbox-group>
                    </el-form-item>
                    <!--提交保存-->
                    <el-form-item>
                        <el-button @click="resetForm('ruleForm')">重置</el-button>
                        <el-button type="warning" @click="submitDraft('ruleForm')">保存草稿</el-button>
                        <el-button type="primary" @click="submitForm('ruleForm')">立即发布</el-button>
                    </el-form-item>
                </el-form>

            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'blogEdit',
    data() {
        return {
            ruleForm: {
                id:'',//博客id（修改使用）
                title: '', //博客标题
                summary: '', //博客摘要
                content: '',//博客内容
                keyword: [], //后台关键字值存储位置
                typeName:'',//类型
                published:0,//发布状态，默认为发布

                commentOpen:1,//是否开启评论
                appreciation:1,//是否开启赞赏
                recommend:1,//是否开启推荐
                shareStatement:1,//是否可转载
                createSign:[], //原创转载默认值
                //博客封面图信息
                blogImg: '',
            },
            imgView:'',
            dialogImageUrl: '',
            dialogVisible: false,
            rules: {
                title: [
                    { required: true, message: '请输入标题', trigger: 'blur' },
                    { min: 3, max: 50, message: '长度在 3 到 25 个字符', trigger: 'blur' }
                ],
                summary: [
                    { required: true, message: '请输入摘要', trigger: 'blur' }
                ],
                content: [
                    {  required: true, message: '请输入内容', trigger: 'blur' }
                ],
                typeName: [
                    {  required: true, message: '请输入博客类型', trigger: 'blur' },
                    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
                ],
                keyword: [
                    { required: true, message: '请输入关键字', trigger: 'blur' },]
            },
            keywordValue:['java','c','springboot'],//博客关键字
            createSignValue:['原创','转载','分享'],//原创转载声明

            checkList: [],//复选框存储数据
            checkRecommend: '推荐',//推荐框
            checkShare: '转载',//转载声明框
            checkPraise :'赞赏',//赞赏框
            checkComment: '评论',//评论框

        }
    },

        methods: {
        //发布
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const g_this = this;
                        //获取作者选择状态情况，赋值
                        let checkList = g_this.checkList;
                        for (let i = 0; i < checkList.length ; i++) {
                            if (checkList[i] === '推荐'){
                                g_this.ruleForm.recommend = 0;
                            }
                            if (checkList[i] === '转载'){
                                g_this.ruleForm.shareStatement = 0;
                            }
                            if (checkList[i] === '赞赏'){
                                g_this.ruleForm.appreciation = 0;
                            }
                            if (checkList[i] === '评论'){
                                g_this.ruleForm.commentOpen = 0;
                            }
                        }
                        //保存发布状态
                        g_this.ruleForm.published = 0;

                        //先判断此时是否是用户强行关闭浏览器，通过残留token强行进入的接口
                        g_this.detectRealBlogger();

                        //发送数据
                        this.axios.post("blog/blog/edit",this.ruleForm,{
                                headers:{
                                    "Authorization":localStorage.getItem("token")
                                }
                        }).then(res =>{
                            console.log(res+"博客编辑")
                            //设置2s跳转
                            this.$message.success('发布成功！即将跳转至博客管理页面...');
                            setTimeout(() =>{
                                g_this.$router.push("/blogs");
                            },2000);
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            //保存草稿
            submitDraft(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const g_this = this;
                        //获取作者选择状态情况，赋值
                        let checkList = g_this.checkList;
                        for (let i = 0; i < checkList.length ; i++) {
                            if (checkList[i] === '推荐'){
                                g_this.ruleForm.recommend = 0;
                            }
                            if (checkList[i] === '转载'){
                                g_this.ruleForm.shareStatement = 0;
                            }
                            if (checkList[i] === '赞赏'){
                                g_this.ruleForm.appreciation = 0;
                            }
                            if (checkList[i] === '评论'){
                                g_this.ruleForm.commentOpen = 0;
                            }
                        }
                        //保存草稿状态
                        g_this.ruleForm.published = 1;


                        //先判断此时是否是用户强行关闭浏览器，通过残留token强行进入的接口
                        g_this.detectRealBlogger();

                        //发送数据
                        this.axios.post("blog/blog/edit",this.ruleForm,{
                            headers:{
                                "Authorization":localStorage.getItem("token")
                            }
                        }).then(res =>{
                            console.log(res+"博客编辑")
                            //设置2s跳转
                            this.$message.success('已保存至草稿，即将跳转至草稿管理页面...');
                            setTimeout(() =>{
                                g_this.$router.push("/drafts");
                            },2000);
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                    console.log(this.ruleForm.published+"----草稿")
                });
            },
            /*上传博客封面图方法*/
            // 上传前校验
            beforeAvatarUpload(file) {
                const isPic = file.type.indexOf('image') >= 0;
                const isLt2M = file.size / 1024 / 1024 < 2;
                if (!isPic) {
                    this.$message.error('只能为图片格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传图片大小不能超过 2MB!');
                }
                return isPic && isLt2M;
            },
            // 上传图片方法
            uploadImage(param){
                    let g_this = this;
                    const formData = new FormData()
                    formData.append('file', param.file)


                    //先判断此时是否是用户强行关闭浏览器，通过残留token强行进入的接口
                    g_this.detectRealBlogger();

                    this.axios.post("/blog/upload",formData,{
                        headers:{
                            "Authorization":localStorage.getItem("token"),
                            'Content-Type': 'multipart/form-data'
                        }
                    }).then((res) => {
                        console.log("文件上传返回：上传图片成功"+res)
                        //图片显示的路径
                        g_this.imgView=g_this.$store.getters.getAddress+res.data.data;
                        //返回需要在数据库储存的相对路径
                        g_this.ruleForm.blogImg=res.data.data;
                        console.log(res.data.data)
                        console.log(g_this.imgView)
                    }).catch(error => {
                        console.log("文件上传异常:图片上传失败"+error)
                    })

            },
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            //移除封面图
            handleRemove(file, fileList) {
                let g_this = this;
                let uri = encodeURI(g_this.ruleForm.blogImg);
                console.log(file+'移除的图片--------');
                this.axios.post("/blog/deleteBlogImg", uri,
                    {
                    headers:{
                        "Authorization":localStorage.getItem("token"),
                    }
                }).then((res) => {
                    if (res.data.data === false){
                        console.log("移除图片失败"+error)
                    }else {
                        console.log("移除图片成功"+res)
                        g_this.imgView='';
                       /* console.log(res.data.data)*/
                    }
                }).catch(error => {
                    console.log("移除图片失败"+error)
                })
            },

             //重置输入栏
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            //判断此时是否是真正博主在登录
            detectRealBlogger(){
                //先判断此时是否是用户强行关闭浏览器，通过残留token强行进入的接口
                let userInfo = window.sessionStorage.getItem("userInfo");
                if (userInfo===null || userInfo ===undefined ||userInfo===''){
                    window.localStorage.removeItem("token");//移除
                    this.$router.push("/fblog");
                    location.reload();
                }
            }
        },
    created() {
        let token = localStorage.getItem("token");
        let userInfo = window.sessionStorage.getItem("userInfo");

        if (token===null || token ===undefined ||token===''){
            this.$message.error('您无权限访问！');
            this.$router.push("/fblog");
            location.reload();
        }else if (userInfo===null || userInfo ===undefined ||userInfo===''){
            this.$message.error('您无权限访问！');
            this.$router.push("/fblog");
            location.reload();
        }else if (this.$store.getters.getUser.id !== 1){
            //代表未登录，移除因直接关闭浏览器残留的token
            window.localStorage.removeItem("token");
            this.$message.error('您无权限访问！');
            this.$router.push("/fblog");
            location.reload();
        }else{
            //是作者，允许访问
            //回显信息
            const blogId= this.$route.params.blogId //从路径获取博客id
            /* console.log(blogId+"博客id")*/
            const g_this = this;
            if(blogId){
                this.axios.get('/blog/detail/'+blogId).then(res =>{
                    const blog = res.data.data;
                    g_this.ruleForm.id = blog.id;
                    g_this.ruleForm.title = blog.title;
                    g_this.ruleForm.summary = blog.summary;
                    g_this.ruleForm.content = blog.content;
                    g_this.ruleForm.typeName = blog.typeName;
                    g_this.ruleForm.keyword = blog.keyword;
                    g_this.ruleForm.createSign = blog.createSign;
                    g_this.imgView= g_this.$store.getters.getAddress+blog.blogImg;
                    g_this.ruleForm.blogImg= blog.blogImg;
                    //回显获取作者选择状态情况
                    if (blog.recommend === 0){
                        g_this.checkList.push('推荐')
                    }
                    if (blog.shareStatement === 0){
                        g_this.checkList.push('转载')
                    }
                    if (blog.appreciation === 0){
                        g_this.checkList.push('赞赏')
                    }
                    if (blog.commentOpen === 0){
                        g_this.checkList.push('评论')
                    }

                });

            }
            //回显关键词
            const a = [];
            this.axios.get('/keyword/get').then(res =>{
                console.log(res+"-----------关键词回显")
                console.log(res.data.data[0].keyword+"-----------关键词回显")
                for (let i = 0; i < res.data.data.length; i++) {
                    a.push(res.data.data[i].keyword);
                    console.log(a)
                }
                g_this.keywordValue = a;
            });
        }
    },
    //检测路由变化，刷新跳转的路由组件
    watch: {
        '$route' (to, from) {
            this.$router.go(0);
        }
    },
};
</script>

<style scoped>
    .my-content{
        text-align: center;

    }

    .container{
        background:rgba(255,255,255,0.7);
    }
    /*博客封面图css*/
    .avatar{
        height:100%;
        width: 100%;
    }
</style>