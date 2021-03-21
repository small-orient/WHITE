package com.baidetu.common.annotation;

import java.lang.annotation.*;

/**
 * 评论防刷注解
 */
@Target({ElementType.METHOD, ElementType.TYPE}) //作用于方法、类
@Retention(RetentionPolicy.RUNTIME) //该注解将被编译器记录在类文件中
@Documented
public @interface CommentLimit {
    //秒
    int seconds();

    //最大执行次数
    int maxCount();
}
