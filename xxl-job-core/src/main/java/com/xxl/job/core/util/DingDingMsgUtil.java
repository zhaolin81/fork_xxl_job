package com.xxl.job.core.util;

import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * 钉钉消息工具类
 * Created by zhusheng on 2017/6/23 0023.
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
