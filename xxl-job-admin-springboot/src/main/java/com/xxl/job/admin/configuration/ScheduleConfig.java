package com.xxl.job.admin.configuration;

import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

/**
 * quartz调度器配置相关
 * Created by zhusheng on 2017/6/26 0026.
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    @Qualifier("c3p0DataSource")
    private DataSource dataSource;

    @Autowired
    private ApplicationContext applicationContext;


    @Bean
    @Lazy(false)
    public SchedulerFactoryBean quartzScheduler(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setStartupDelay(20);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setApplicationContext(applicationContext);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        schedulerFactoryBean.setConfigLocation(resolver.getResource("classpath:quartz.properties"));
        return schedulerFactoryBean;
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public XxlJobDynamicScheduler xxlJobDynamicScheduler(SchedulerFactoryBean quartzScheduler){
        XxlJobDynamicScheduler xxlJobDynamicScheduler = new XxlJobDynamicScheduler();
        xxlJobDynamicScheduler.setScheduler(quartzScheduler.getObject());
        return xxlJobDynamicScheduler;
    }
}
