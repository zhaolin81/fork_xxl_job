package com.xxl.job.admin.core.status;

import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;

/**
 * Job失败监听器默认适配器
 * Created by zhaolin on 6/23/2017.
 */

public abstract class JobStatusAdapter implements JobStatusListener {
    public void onJobFail(XxlJobInfo jobInfo, XxlJobLog jobLog){}
    public void onJobSuccess(XxlJobInfo jobInfo, XxlJobLog jobLog){}
}
