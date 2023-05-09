package io.github.changebooks.rocketmq;

import com.aliyun.openservices.ons.api.Message;
import io.github.changebooks.log.LogId;
import io.github.changebooks.log.LogParentId;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 日志id
 *
 * @author changebooks@qq.com
 */
public final class RocketMqLogId {
    /**
     * 键名
     */
    public static final String KEY_NAME = "log_id";

    private RocketMqLogId() {
    }

    /**
     * 消费之前
     * 从已接收消息获取日志id，设置日志上下文
     *
     * @param message 已接收的消息
     */
    public static void onConsume(Message message) {
        Objects.requireNonNull(message, "message can't be null");

        String logId = message.getUserProperties(KEY_NAME);
        if (StringUtils.hasText(logId)) {
            LogParentId.set(logId);
        }

        LogId.init();
    }

    /**
     * 发送之前
     * 从日志上下文获取日志id，设置待发送消息
     *
     * @param message 待发送的消息
     */
    public static void onSend(Message message) {
        Objects.requireNonNull(message, "message can't be null");

        String logId = LogId.get();
        if (StringUtils.hasText(logId)) {
            message.putUserProperties(KEY_NAME, logId);
            return;
        }

        logId = message.getUserProperties(KEY_NAME);
        if (StringUtils.hasText(logId)) {
            return;
        }

        LogId.init();
        logId = LogId.get();
        message.putUserProperties(KEY_NAME, logId);
    }

}
