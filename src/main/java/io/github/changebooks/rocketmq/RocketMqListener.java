package io.github.changebooks.rocketmq;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.Subscription;

/**
 * 监听消息
 *
 * @author changebooks@qq.com
 */
public interface RocketMqListener extends MessageListener {
    /**
     * 订阅
     * 主题、表达式、标签
     *
     * @return {@link Subscription} 实例
     */
    Subscription subscription();

}
