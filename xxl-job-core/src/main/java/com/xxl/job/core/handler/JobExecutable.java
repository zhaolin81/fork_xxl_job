package com.xxl.job.core.handler;

import com.xxl.job.core.biz.model.ReturnT;

/**
 * 任务接口，实现任务的具体业务逻辑
 * Created by zhaolin on 6/20/2017.
 */
public interface JobExecutable {
    /**
     * job handler
     * @param params
     * @return
     * @throws Exception
     */
    ReturnT<String> execute(String... params) throws Exception;
}
