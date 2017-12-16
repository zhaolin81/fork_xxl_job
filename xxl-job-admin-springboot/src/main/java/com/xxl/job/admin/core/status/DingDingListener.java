package com.xxl.job.admin.core.status;

import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.status.annotation.JobStatus;
import com.xxl.job.admin.core.util.PropertiesUtil;
import com.xxl.job.core.util.DingDingMsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 钉钉群任务告警处理器
 * Created by zhaolin on 6/23/2017.
 */
@JobStatus
@Component
public class DingDingListener extends JobStatusAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DingDingListener.class);
    private String dingdingToken = PropertiesUtil.getString("xxl.job.dingding.token");

    @Override
    public void onJobFail(XxlJobInfo jobInfo, XxlJobLog jobLog) {
        String content = jobInfo.getJobDesc()+":"+jobLog.getHandleCode()+":"+jobLog.getHandleMsg()+jobLog.getTriggerCode()
                +":"+jobLog.getTriggerMsg();
        logger.info("onJobFail:"+content);
        DingDingMsgUtil.send(dingdingToken, content);
    }
}
