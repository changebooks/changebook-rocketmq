package io.github.changebooks.rocketmq;

import com.aliyun.openservices.ons.api.Message;
import io.github.changebooks.log.LogTraceId;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 追溯id
 *
 * @author changebooks@qq.com
 */
public final class RocketMqTraceId {
    /**
     * 键名
     */
    public static final String KEY_NAME = "log_tid";

    private RocketMqTraceId() {
    }

    /**
     * 消费之前
     * 从已接收消息获取追溯id，设置日志上下文
     *
     * @param message 已接收的消息
     */
    public static void onConsume(Message message) {
        Objects.requireNonNull(message, "message can't be null");

        String traceId = message.getUserProperties(KEY_NAME);
        if (StringUtils.hasText(traceId)) {
            LogTraceId.set(traceId);
        } else {
            LogTraceId.init();
        }
    }

    /**
     * 发送之前
     * 从日志上下文获取追溯id，设置待发送消息
     *
     * @param message 待发送的消息
     */
    public static void onSend(Message message) {
        Objects.requireNonNull(message, "message can't be null");

        String traceId = LogTraceId.get();
        if (StringUtils.hasText(traceId)) {
            message.putUserProperties(KEY_NAME, traceId);
            return;
        }

        traceId = message.getUserProperties(KEY_NAME);
        if (StringUtils.hasText(traceId)) {
            return;
        }

        LogTraceId.init();
        traceId = LogTraceId.get();
        message.putUserProperties(KEY_NAME, traceId);
    }

}
