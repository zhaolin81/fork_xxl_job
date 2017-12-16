package com.xxl.job.admin.core.status.annotation;

import java.lang.annotation.*;

/**
 * 任务执行结果状态监听注解
 * Created by zhaolin on 6/23/2017.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JobStatus {
    String ALL="*";

    /**
     * handler名称，常量JobStatus.ALL说明监听所有任务的事件
     * @return
     */
    String value() default ALL;

}
