package io.github.changebooks.rocketmq;

import com.aliyun.openservices.ons.api.Admin;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.spi.ConsumerInterceptor;
import com.aliyun.openservices.ons.api.spi.InvocationContext;
import io.github.changebooks.log.LogClear;
import io.github.changebooks.log.LogId;
import io.github.changebooks.log.LogTraceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 拦截接收消息
 * 从已接收消息获取日志信息，设置日志上下文
 *
 * @author changebooks@qq.com
 */
public class LogConsumerInterceptor implements ConsumerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogConsumerInterceptor.class);

    @Override
    public boolean preHandle(InvocationContext invocationContext, Admin instance) {
        if (invocationContext == null) {
            return true;
        }

        try {
            processLog(invocationContext);
        } catch (Throwable tr) {
            LOGGER.error("preHandle failed, throwable: ", tr);
        }

        return true;
    }

    @Override
    public void postHandle(InvocationContext invocationContext, Admin instance) {
        LogClear.clear();
    }

    /**
     * 消费之前
     * 从已接收消息获取日志信息，设置日志上下文
     *
     * @param context 调用上下文
     */
    public void processLog(InvocationContext context) {
        Message msg = RocketMqMessage.getMessage(context);

        if (msg == null) {
            LogTraceId.init();
            LogId.init();
        } else {
            RocketMqTraceId.onConsume(msg);
            RocketMqLogId.onConsume(msg);
        }
    }

}
