package com.baidetu.controller;
import com.baidetu.config.BaiDuConfig;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.*;

/**
 * 调用百度统计API
 */
@RestController
public class CountController {

    @Resource
    private BaiDuConfig baiDuConfig;
    //样例
    @GetMapping(value = "/getTimeTrendRpt",produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getTimeTrendRpt()  {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -1);// 昨天

        String today = Arrays.toString(String.format("%tF", now).split("-")),
                yesterday = Arrays.toString(String.format("%tF", calendar.getTime()).split("-"));
        System.out.println("today:"+today);

        return baiDuConfig.getAdjacentDateVisitorData(yesterday,today);
    }
}
