package com.xxl.job.admin.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.xxl.job.admin.core.util.PropertiesUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

/**
 * 持久层相关配置
 */

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class PersistenceConfiguration {

	private static final org.slf4j.Logger LOGGER =LoggerFactory.getLogger(PersistenceConfiguration.class);

    @Value("${xxl.job.db.driverClass}")
    private String driverClass;

    @Value("${xxl.job.db.user}")
    private String userName;

    @Value("${xxl.job.db.password}")
    private String password;

    @Value("${xxl.job.db.url}")
    private String url;

    @Value("${spring.datasource.initialPoolSize}")
    private int initialPoolSize;

    @Value("${spring.datasource.minPoolSize}")
    private int minPoolSize;

    @Value("${spring.datasource.maxPoolSize}")
    private int maxPoolSize;

    @Value("${spring.datasource.maxIdleTime}")
    private int maxIdleTime;

    @Value("${spring.datasource.acquireIncrement}")
    private int acquireIncrement;

    @Value("${spring.datasource.acquireRetryAttempts}")
    private int acquireRetryAttempts;

    @Value("${spring.datasource.acquireRetryDelay}")
    private int acquireRetryDelay;

    @Value("${spring.datasource.idleConnectionTestPeriod}")
    private int idleConnectionTestPeriod;

    @Value("${spring.datasource.preferredTestQuery}")
    private String preferredTestQuery;

    @Value("${spring.datasource.testConnectionOnCheckout}")
    private boolean testConnectionOnCheckout;

    @Value("${spring.datasource.testConnectionOnCheckin}")
    private boolean testConnectionOnCheckin;

    @Bean(name = "c3p0DataSource",destroyMethod = "close")
    public DataSource dataSource(){
        ComboPooledDataSource c3p0DataSource = null;
        try {
            c3p0DataSource = new ComboPooledDataSource();
            //配置数据库连接基础信息
            c3p0DataSource.setDriverClass(driverClass);
            c3p0DataSource.setUser(userName);
            c3p0DataSource.setPassword(password);
            c3p0DataSource.setJdbcUrl(url);
            //初始化时获取的连接数，取值应在minPoolSize与maxPoolSize之间。Default: 3
            c3p0DataSource.setInitialPoolSize(initialPoolSize);
            //连接池中保留的最小连接数
            c3p0DataSource.setMinPoolSize(minPoolSize);
            //连接池中保留的最大连接数。Default: 15
            c3p0DataSource.setMaxPoolSize(maxPoolSize);
            //最大空闲时间,60秒内未使用则连接被丢弃。若为0则永不丢弃
            c3p0DataSource.setMaxIdleTime(maxIdleTime);
            //当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。Default: 3
            c3p0DataSource.setAcquireIncrement(acquireIncrement);
            //定义在从数据库获取新连接失败后重复尝试的次数。Default: 30
            c3p0DataSource.setAcquireRetryAttempts(acquireRetryAttempts);
            //连接池在获得新连接时的间隔时间
            c3p0DataSource.setAcquireRetryDelay(acquireRetryDelay);
            //每60秒检查所有连接池中的空闲连接。Default: 0
            c3p0DataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);

            c3p0DataSource.setPreferredTestQuery(preferredTestQuery);
            c3p0DataSource.setTestConnectionOnCheckout(testConnectionOnCheckout);
            c3p0DataSource.setTestConnectionOnCheckin(testConnectionOnCheckin);
        } catch (PropertyVetoException e) {
            LOGGER.error("build datasoure exception ",e.getMessage());
        }
        return c3p0DataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis-mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactoryBean){
        return new SqlSessionTemplate(sqlSessionFactoryBean);
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

    @Bean(name = "transactionInterceptor")
    public TransactionInterceptor transactionInterceptor(
            PlatformTransactionManager transactionManager) {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        // 事物管理器
        transactionInterceptor.setTransactionManager(transactionManager);

        //事务属性
        Properties transactionAttributes = new Properties();
        transactionAttributes.setProperty("detail*","PROPAGATION_SUPPORTS");
        transactionAttributes.setProperty("visit*", "PROPAGATION_SUPPORTS");
        transactionAttributes.setProperty("get*", "PROPAGATION_SUPPORTS");
        transactionAttributes.setProperty("find*", "PROPAGATION_SUPPORTS");
        transactionAttributes.setProperty("check*", "PROPAGATION_SUPPORTS");
        transactionAttributes.setProperty("list*", "PROPAGATION_SUPPORTS");
        transactionAttributes.setProperty("*", "PROPAGATION_REQUIRED,-exception");

        transactionInterceptor.setTransactionAttributes(transactionAttributes);
        return transactionInterceptor;
    }

    @Bean
    public BeanNameAutoProxyCreator transactionAutoProxy() {
        BeanNameAutoProxyCreator transactionAutoProxy = new BeanNameAutoProxyCreator();
        transactionAutoProxy.setProxyTargetClass(true);
        transactionAutoProxy.setBeanNames("*Service");
        transactionAutoProxy.setInterceptorNames("transactionInterceptor");
        return transactionAutoProxy;
    }

}
