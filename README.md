# 《分布式任务调度平台XXL-JOB》
forked from

https://github.com/xuxueli/xxl-job

##基于xxl-job 1.7.2 扩展以下功能
- 1、调度平台迁移至Springboot 1.4.5.RELEASE，配置改为javaconfig，内嵌容器启动
```
java -jar  xxl-job-admin-springboot.war
```
- 2、core模块新增JobExecutable接口，方便已有调度业务平滑迁移至调度平台
```
package com.xxl.job.core.handler;

import com.xxl.job.core.biz.model.ReturnT;

/**
 * 任务接口，实现任务的具体业务逻辑
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
```
- 3、core模块新增钉钉群告警消息工具类
```
package com.xxl.job.core.util;

import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * 钉钉消息工具类
 */
public class DingDingMsgUtil {
    public static String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=";

    private static Logger logger = LoggerFactory.getLogger(DingDingMsgUtil.class);

    public static void send(String token, String content){
        try {
            String textMsg = "{ \"msgtype\": \"text\", \"text\": {\"content\": \""+content+"\"}}";
            HttpClientUtil.postRequest(WEBHOOK_TOKEN+token, textMsg.getBytes(Charset.forName("UTF-8")), ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            logger.error("发送钉钉消息异常",e);
        }
    }
}
```
- 4、admin模块新增任务执行状态回调接口，提供默认实现钉钉群告警监听器；
```
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
```
- 5、admin模块新增暂停所有任务和恢复所有任务功能；
```
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {
    @RequestMapping("/pauseAll")
    @ResponseBody
    public ReturnT<String> pause(String jobGroup) {
        return xxlJobService.pauseAll(jobGroup);
    }
    
    @RequestMapping("/resumeAll")
    @ResponseBody
    public ReturnT<String> resume(String jobGroup) {
        return xxlJobService.resumeAll(jobGroup);
    }
}
```
- 6、admin模块新增accesstoken实现api直接访问

```
【xxl-job-admin.properties】
#API accesstoken，运维工具，通过token直接访问api
xxl.job.access.token=accesstoken

# dingding token
# 钉钉告警token，钉钉群内机器人设置可获取到

xxl.job.dingding.token=xxxtoken
```

##### zhaolin 48182099@qq.com
##### forked from 
https://github.com/xuxueli/xxl-job
