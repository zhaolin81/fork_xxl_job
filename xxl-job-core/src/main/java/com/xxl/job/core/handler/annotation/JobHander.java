package com.xxl.job.core.handler.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * annotation for job handler
 * @author 2016-5-17 21:06:49
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
public @interface JobHander {
    /**
     * job handler 名称
     * @return
     */
    String value() default "";
    
}
