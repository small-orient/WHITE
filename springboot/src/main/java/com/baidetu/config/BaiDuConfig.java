package com.baidetu.config;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * 百度统计配置类
 */
@Data
@Component
@Slf4j
public class BaiDuConfig {
    //配置值
    @Value("${BaiDu.username}")
    private String username;
    @Value("${BaiDu.password}")
    private String password;
    @Value("${BaiDu.token}")
    private String count_token;
    //请求URL
    private String getSiteIdUrl="https://api.baidu.com/json/tongji/v1/ReportService/getSiteList";//获取站点ID
    private String getSiteDataUrl="https://api.baidu.com/json/tongji/v1/ReportService/getData";//获取访客数据

     //常量值
     public static String USERNAME = "USERNAME";
     public static String PASSWORD = "PASSWORD";
     public static String COUNT_TOKEN = "COUNT_TOKEN";


    //获取属性值
    public String getValue(String name){
        String result = null;
        if (name.equals(USERNAME)){
            result = username;
        }
        if (name.equals(PASSWORD)){
            result =password;
        }
        if (name.equals(COUNT_TOKEN)){
            result =count_token;
        }
        return result;
    }

    /**
     * 获取请求头
     *
     * @return
     */
    public JSONObject getHeader() {
        JSONObject header = new JSONObject();
        header.put("username",username);//用户名
        header.put("password", password);//用户密码
        header.put("token", count_token);//申请到的token
        header.put("account_type", "1");
        return header;
    }

    //获取站点ID
    public List<Object> getSiteList() {
        try {
            //头数据
            JSONObject header = getHeader();
            //封装请求参数
            JSONObject params = new JSONObject();
            params.put("header", header);
            //调用百度获取站点api
            String s = HttpUtil.post(getSiteIdUrl, params.toString());
            //解析返回数据获取site_id
            JSONObject outerObject = JSONUtil.parseObj(s);
            JSONObject data = JSONUtil.parseObj(outerObject.get("body"));
            System.out.println("data:"+data);
            JSONObject dataObject = JSONUtil.parseObj(data);
            JSONArray dataArray = JSONUtil.parseArray(dataObject.get("data"));
            System.out.println("dataArray:"+dataArray);
            JSONObject jsonObject = JSONUtil.parseObj(dataArray.get(0));
            JSONArray list = JSONUtil.parseArray(jsonObject.get("list"));
            List<Object> id = new ArrayList<>();//返回站点ID的list集合
            //遍历出站点
            for (Object o : list) {
                if (o!=null){
                    JSONObject obj = JSONUtil.parseObj(o);
                    Object siteId = obj.get("site_id");
                    id.add(siteId);
                }
            }
            return id;
        } catch (Exception e) {
            // TODO: handle exception
           log.error("e-----------------------{}:站点ID获取失败！");
        }
        return null;
    }

    //获取指定日期的访客数据
    public String getAdjacentDateVisitorData(String start,String end){

        List<Object> siteList = getSiteList();
        JSONObject body = new JSONObject();
        assert siteList != null;
        body.put("siteId",siteList.get(0));//单站点就获取一个就行
        body.put("method","overview/getTimeTrendRpt");//需要获取的数据
        body.put("start_date",start);
        body.put("end_date",end);
        body.put("metrics","pv_count,visitor_count,ip_count,bounce_ratio,avg_visit_time");//指标,数据单位

        JSONObject header = getHeader();
        JSONObject params = new JSONObject();
        params.put("header", header);
        params.put("body",body);
        String s = HttpUtil.post(getSiteDataUrl, params.toString());
        return s;
    }

    //获取指定日期网站概况(来源网站、搜索词、入口页面、受访页面)
    public String getWebsiteOverview(String start,String end){

        List<Object> siteList = getSiteList();
        JSONObject body = new JSONObject();
        assert siteList != null;
        body.put("siteId",siteList.get(0));
        body.put("method","overview/getCommonTrackRpt");//需要获取的数据
        body.put("start_date",start);
        body.put("end_date",end);

        JSONObject header = getHeader();
        JSONObject params = new JSONObject();
        params.put("header", header);
        params.put("body",body);
        String s = HttpUtil.post(getSiteDataUrl, params.toString());
        return s;
    }

    //获取指定日期内的pv/uv趋势图
    public String getPageviewsAndVisitorData(String start,String end){

        List<Object> siteList = getSiteList();
        JSONObject body = new JSONObject();
        assert siteList != null;
        body.put("siteId",siteList.get(0));
        body.put("method", "trend/time/a");
        body.put("metrics", "pv_count,visit_count");
        body.put("max_results", "0");
        body.put("gran", "day");
        body.put("start_date",start);
        body.put("end_date",end);

        JSONObject header = getHeader();
        JSONObject params = new JSONObject();
        params.put("header", header);
        params.put("body",body);
        String s = HttpUtil.post(getSiteDataUrl, params.toString());
        return s;
    }
}
