package com.xxl.job.core.log;

import org.apache.commons.logging.Log;

/**
 * Created by zhaolin on 9/7/2017.
 */
public class CompositeLog implements Log {

    private Log log;

    public CompositeLog(Log log){
        this.log = log;
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return log.isFatalEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void trace(Object o) {
        XxlJobLogger.log(o.toString());
        log.trace(o);
    }

    @Override
    public void trace(Object o, Throwable throwable) {
        XxlJobLogger.log(o.toString()+"\r\n"+throwable.getMessage());
        log.trace(o,throwable);
    }

    @Override
    public void debug(Object o) {
        XxlJobLogger.log(o.toString());
        log.debug(o);
    }

    @Override
    public void debug(Object o, Throwable throwable) {
        XxlJobLogger.log(o.toString()+"\r\n"+throwable.getMessage());
        log.debug(o,throwable);
    }

    @Override
    public void info(Object o) {
        XxlJobLogger.log(o.toString());
        log.info(o);
    }

    @Override
    public void info(Object o, Throwable throwable) {
        XxlJobLogger.log(o.toString()+"\r\n"+throwable.getMessage());
        log.info(o,throwable);
    }

    @Override
    public void warn(Object o) {
        XxlJobLogger.log(o.toString());
        log.warn(o);
    }

    @Override
    public void warn(Object o, Throwable throwable) {
        XxlJobLogger.log(o.toString()+"\r\n"+throwable.getMessage());
        log.warn(o,throwable);
    }

    @Override
    public void error(Object o) {
        XxlJobLogger.log(o.toString());
        log.error(o);
    }

    @Override
    public void error(Object o, Throwable throwable) {
        XxlJobLogger.log(o.toString()+"\r\n"+throwable.getMessage());
        log.error(o,throwable);
    }

    @Override
    public void fatal(Object o) {
        XxlJobLogger.log(o.toString());
        log.fatal(o);
    }

    @Override
    public void fatal(Object o, Throwable throwable) {
        XxlJobLogger.log(o.toString()+"\r\n"+throwable.getMessage());
        log.fatal(o,throwable);
    }
}
