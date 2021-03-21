package com.baidetu.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 计算用户上线日期差工具类
 */
@Slf4j
public class DateCountUtils {

    private static int days; //天数

    private static int hours; //时

    private static int minutes; //分

    private static int seconds; //秒


    public static Integer getDay(Date first, Date second) {
        //得到两个日期对象的总毫秒数
        long firstDateMilliSeconds = first.getTime();
        long secondDateMilliSeconds = second.getTime();

        //得到两者之差
        long milliSeconds = firstDateMilliSeconds - secondDateMilliSeconds;
        //毫秒转为秒
        int totalSeconds = (int) (milliSeconds / 1000);

        //得到总天数
        days = totalSeconds / (3600 * 24);
        int days_remains = totalSeconds % (3600 * 24);

        //得到总小时数
        hours = days_remains / 3600;
        int remains_hours = days_remains % 3600;

        //得到分种数
        minutes = remains_hours / 60;

        //得到总秒数
        seconds = remains_hours % 60;

        //打印结果
        //第一个比第二个多32天2小时3分4秒
        log.info("用户已间隔:");
        log.info(days + "天" + hours + "小时" + minutes + "分" + seconds + "秒" + "未进行登录");
        return days;
    }

}
