package com.xxl.job.admin.core.status;

import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;

/**
 * Job执行状态监听器
 * Created by zhaolin on 6/23/2017.
 */

public interface JobStatusListener {
    /**
     * 执行失败回调
     * @param jobInfo
     * @param jobLog
     */
    void onJobFail(XxlJobInfo jobInfo,XxlJobLog jobLog);

    /**
     * 执行成功回调
     * @param jobInfo
     * @param jobLog
     */
    void onJobSuccess(XxlJobInfo jobInfo,XxlJobLog jobLog);
}
