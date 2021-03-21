<template>
    <div class="footer" >
        <div class="footer-item">
         <!--   <div class="copyright">本站已苟活....</div>-->
        </div>
        <div class="copyright" >
            Copyright © 2020 by
            <a target="_blank" class="out-link" href="https://www.baidetu.cn">www.baidetu.cn</a>
            . All rights reserved.
            <div >
                <a target="_blank"
                   href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=45010702001595"
                   style="display:inline-block;text-decoration:none;height:20px;line-height:20px;">
                    <img :src="beianUrl" style="float:left;" alt=""/>
                    <p style="float:left;height:20px;line-height:20px;margin: 0 0 0 5px; color:#939393;">
                        桂公网安备 45010702001595号 | 桂ICP备2020008625号-1</p>
                </a>
            </div>
            <a target="_blank" class="out-link" href="https://beian.miit.gov.cn/">
                <p style="float:left;height:20px;line-height:20px;margin: 0 0 0 5px; color:#939393;">
                    </p>
               </a>
        </div>

    </div>
</template>
<script>
    import beian from "../../assets/img/beian.png";
    export default {
        name: "FrontFooter",
        data(){
            return{
                beianUrl: beian,
            }
        },
    }
</script>

<style scoped>

    .footer{
    padding: 2%;
    border-top: 1px solid #F7F7F7;
    font-size: 13px;
    color: #9c9c9c;
    background-color: rgb(58, 62, 74);
    }

    .copyright{
        text-align: center;
    }



</style>