package io.github.changebooks.rocketmq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.spi.InvocationContext;
import com.aliyun.openservices.shade.com.google.common.base.Optional;

import java.util.List;

/**
 * 消息
 *
 * @author changebooks@qq.com
 */
public final class RocketMqMessage {

    private RocketMqMessage() {
    }

    /**
     * 获取消息列表
     *
     * @param context 调用上下文
     * @return 消息列表
     */
    public static List<Message> getMessages(InvocationContext context) {
        if (context == null) {
            return null;
        }

        Optional<List<Message>> optional = context.getMessages();
        if (optional != null) {
            return optional.orNull();
        } else {
            return null;
        }
    }

    /**
     * 获取一个消息
     *
     * @param context 调用上下文
     * @return 首个消息
     */
    public static Message getMessage(InvocationContext context) {
        if (context == null) {
            return null;
        }

        List<Message> messages = RocketMqMessage.getMessages(context);
        if (messages != null) {
            return getMessage(messages);
        } else {
            return null;
        }
    }

    /**
     * 获取一个消息
     *
     * @param messages 消息列表
     * @return 首个消息
     */
    public static Message getMessage(List<Message> messages) {
        if (messages == null) {
            return null;
        }

        for (Message msg : messages) {
            if (msg != null) {
                return msg;
            }
        }

        return null;
    }

}
