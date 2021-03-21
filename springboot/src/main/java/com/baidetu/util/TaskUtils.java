package com.baidetu.util;

import com.baidetu.entity.HUser;
import com.baidetu.service.HUserService;
import com.baidetu.service.PaginationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务类：定时储存博客点赞数
 */
@Component
@Slf4j
public class TaskUtils {

    @Resource
    private PaginationService paginationService;

    @Resource
    private HUserService userService;

    public static TaskUtils taskUtils;

    @PostConstruct
    public void init() {
        taskUtils = this;
        taskUtils.paginationService = this.paginationService;
        taskUtils.userService = this.userService;
    }

    /**
     * 定时更新博客点赞数
     * 2小时执行一次
     *
     * @throws InterruptedException
     */
    /* @Scheduled(cron = "0 0 * * * ?") //每小时*/
    /* @Scheduled(fixedRate = 20000)  //20S*/
    @Scheduled(cron = "0 0 */2 * * ?")
    public static void taskLikes() throws InterruptedException {
        taskUtils.paginationService.saveLike();
        log.info("-------------2小时一次,博客点赞更新----------------");
    }

    /**
     * 定时冻结账户
     * 一天执行一次
     *
     * @throws InterruptedException
     */
    /*  @Scheduled(fixedRate = 30000)  //30S*/
    @Scheduled(cron = "0 15 10 ? * *") //每天上午10:15触发
    public static void taskUser() throws InterruptedException {
        List<HUser> userList = taskUtils.userService.list(
                new QueryWrapper<HUser>().eq("locked", 0));
        for (HUser user : userList
        ) {
            Integer day = DateCountUtils.getDay(user.getLoginTime(), user.getLastTime());
            //如果用户最后一次上线时间距离今天超过180天，那么冻结该账户
            if (day >= 180) {
                taskUtils.userService.lockedForUser(user.getId(), 1);
            }
        }
        log.info("-------------每天一次,冻结账户检查----------------");
    }


}
