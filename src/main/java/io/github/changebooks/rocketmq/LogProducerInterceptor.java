package io.github.changebooks.rocketmq;

import com.aliyun.openservices.ons.api.Admin;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.spi.InvocationContext;
import com.aliyun.openservices.ons.api.spi.ProducerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 拦截发送消息
 * 从日志上下文获取日志信息，设置待发送消息
 *
 * @author changebooks@qq.com
 */
public class LogProducerInterceptor implements ProducerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogProducerInterceptor.class);

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
    }

    /**
     * 发送之前
     * 从日志上下文获取日志信息，设置待发送消息
     *
     * @param context 调用上下文
     */
    public void processLog(InvocationContext context) {
        List<Message> messages = RocketMqMessage.getMessages(context);
        if (messages == null) {
            return;
        }

        for (Message msg : messages) {
            if (msg == null) {
                continue;
            }

            RocketMqTraceId.onSend(msg);
            RocketMqLogId.onSend(msg);
        }
    }

}
