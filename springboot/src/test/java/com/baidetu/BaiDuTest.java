package com.baidetu;

import com.baidetu.config.BaiDuConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Slf4j
@SpringBootTest
public class BaiDuTest {

    @Resource
    private BaiDuConfig baiDuConfig;

    //获取站点ID
    @Test
    void test01()  {
        List<Object> siteList = baiDuConfig.getSiteList();
        System.out.println(siteList);
    }

    //获取网站概况：来源网站、搜索词、入口页面、受访页面
    @Test
    void test02() throws IOException {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -1);// 昨天
        StringBuilder builder = new StringBuilder();
        for (String s : String.format("%tF", now).split("-")) {
            builder.append(s);
        }
        StringBuilder builder2 = new StringBuilder();
        for (String s : String.format("%tF", calendar.getTime()).split("-")) {
            builder2.append(s);
        }
        String today = builder.toString(),
                yesterday =builder2.toString();
        String s = baiDuConfig.getAdjacentDateVisitorData(yesterday,today);
        System.out.println(s);
        //{"header":{"desc":"success","failures":[],"oprs":1,"succ":1,"oprtime":0,"quota":1,"rquota":200000000,"status":0},"body":{"data":[{"result":{"timeSpan":["2021/06/15 - 2021/06/16"],"fields":["simple_date_title","pv_count","visitor_count","ip_count","bounce_ratio","avg_visit_time"],"items":[[["2021/06/15"],["2021/06/16"]],[[104,1,1,0,2609],[2,1,1,0,14]],[],[]]}}]}}
    }

    //获取 pv/uv趋势图
    @Test
    void test03() throws IOException {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -1);// 昨天
        StringBuilder builder = new StringBuilder();
        for (String s : String.format("%tF", now).split("-")) {
            builder.append(s);
        }
        StringBuilder builder2 = new StringBuilder();
        for (String s : String.format("%tF", calendar.getTime()).split("-")) {
            builder2.append(s);
        }
        String today = builder.toString(),
                yesterday =builder2.toString();
        System.out.println("today:"+today);

        String s = baiDuConfig.getWebsiteOverview(yesterday, today);
        System.out.println(s);
        //{"header":{"desc":"success","failures":[],"oprs":1,"succ":1,"oprtime":0,"quota":1,"rquota":200000000,"status":0},"body":{"data":[{"result":{"sourceSite":{"fields":["simple_link_title","pv_count","ratio"],"items":[["直接访问",106,100]]},"visitPage":{"fields":["simple_visitpage_title","pv_count","ratio"],"items":[["https://www.baidetu.cn/#/fblog","30",28.3],["https://www.baidetu.cn/#/link","13",12.26],["https://www.baidetu.cn/#/login","12",11.32],["https://www.baidetu.cn/#/ftype","12",11.32],["https://www.baidetu.cn/#/farchive","10",9.43],["https://www.baidetu.cn/#/fwall","10",9.43],["https://www.baidetu.cn/#/about","6",5.66],["https://www.baidetu.cn/#/fregistered","5",4.72],["https://www.baidetu.cn/#/fretrieve","3",2.83],["https://www.baidetu.cn/#/fblog/5","1",0.94]]},"landingPage":{"fields":["simple_landingpage_title","pv_count","ratio"],"items":[["https://www.baidetu.cn/#/fblog","104",98.11],["https://www.baidetu.cn","2",1.89]]},"word":{"fields":["simple_searchword_title","pv_count","ratio"],"items":[]},"visitType":{"oldVisitor":{"pv_count":106,"visitor_count":2,"avg_visit_time":1312,"bounce_ratio":0,"avg_visit_pages":53,"ratio":100},"newVisitor":{"pv_count":"--","visitor_count":"--","avg_visit_time":"--","bounce_ratio":"--","avg_visit_pages":"--","ratio":0}}}}]}}
    }

    //获取 pv/uv趋势图
    @Test
    void test04() throws IOException {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -1);// 昨天
        StringBuilder builder = new StringBuilder();
        for (String s : String.format("%tF", now).split("-")) {
            builder.append(s);
        }
        StringBuilder builder2 = new StringBuilder();
        for (String s : String.format("%tF", calendar.getTime()).split("-")) {
            builder2.append(s);
        }
        String today = builder.toString(),
                yesterday =builder2.toString();
        System.out.println("today:"+today);
        String s = baiDuConfig.getPageviewsAndVisitorData(yesterday, today);
        System.out.println(s);
        //{"header":{"desc":"success","failures":[],"oprs":1,"succ":1,"oprtime":0,"quota":1,"rquota":200000000,"status":0},"body":{"data":[{"result":{"sourceSite":{"fields":["simple_link_title","pv_count","ratio"],"items":[["直接访问",106,100]]},"visitPage":{"fields":["simple_visitpage_title","pv_count","ratio"],"items":[["https://www.baidetu.cn/#/fblog","30",28.3],["https://www.baidetu.cn/#/link","13",12.26],["https://www.baidetu.cn/#/login","12",11.32],["https://www.baidetu.cn/#/ftype","12",11.32],["https://www.baidetu.cn/#/farchive","10",9.43],["https://www.baidetu.cn/#/fwall","10",9.43],["https://www.baidetu.cn/#/about","6",5.66],["https://www.baidetu.cn/#/fregistered","5",4.72],["https://www.baidetu.cn/#/fretrieve","3",2.83],["https://www.baidetu.cn/#/fblog/5","1",0.94]]},"landingPage":{"fields":["simple_landingpage_title","pv_count","ratio"],"items":[["https://www.baidetu.cn/#/fblog","104",98.11],["https://www.baidetu.cn","2",1.89]]},"word":{"fields":["simple_searchword_title","pv_count","ratio"],"items":[]},"visitType":{"oldVisitor":{"pv_count":106,"visitor_count":2,"avg_visit_time":1312,"bounce_ratio":0,"avg_visit_pages":53,"ratio":100},"newVisitor":{"pv_count":"--","visitor_count":"--","avg_visit_time":"--","bounce_ratio":"--","avg_visit_pages":"--","ratio":0}}}}]}}
    }
}
