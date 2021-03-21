package com.baidetu.common.aop;

import com.baidetu.common.Result;
import com.baidetu.common.annotation.CommentLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

/**
 * 评论限制切面
 */
@Aspect
@Component
public class CommentLimitAspect {

    //注解使用的地方为切入点
    @Pointcut("@annotation(commentLimit)")
    public void pointCut(CommentLimit commentLimit) {
    }

    //使用环绕通知
    @Around(value = "pointCut(commentLimit)", argNames = "pjp,commentLimit")
    public Object around(ProceedingJoinPoint pjp, CommentLimit commentLimit) throws Throwable {

        //获取限制时间
        int seconds = commentLimit.seconds();
        //最大次数
        int maxCount = commentLimit.maxCount();
        //redis
        Jedis jedis = new Jedis();
        //获取IP
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        //查看redis中该IP提交的次数
        String s = jedis.get(ip);
        if (s == null || "".equals(s)) {
            //表示在限制时间内第一次评论;
            jedis.setex(ip, seconds, "1");
        } else if (Integer.parseInt(s) < maxCount) {
            //在规定时间内评论次数没有超过限制
            jedis.setex(ip, seconds, String.valueOf(Integer.parseInt(s) + 1));
        } else {
            System.out.println("--------------------" + s + "--------------------");
            return Result.failure(400, "您的请求太快了~", null);
        }
        Object[] args = pjp.getArgs();
        //获得方法执行后的返回值
        return pjp.proceed(args);
    }
}
