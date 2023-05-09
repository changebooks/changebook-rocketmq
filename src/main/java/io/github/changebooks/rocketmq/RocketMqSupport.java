package io.github.changebooks.rocketmq;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 通过默认的方法，或重写的子方法，创建实例
 * <pre>
 * {@link ProducerBean}
 * {@link ConsumerBean}
 * </pre>
 *
 * @author changebooks@qq.com
 */
public class RocketMqSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqSupport.class);

    /**
     * 发送消息
     *
     * @param producer 发送消息配置
     * @return {@link ProducerBean} 实例
     */
    public ProducerBean producerBean(RocketMqProperties.Producer producer) {
        ProducerBean result = new ProducerBean();

        if (producer != null) {
            Properties properties = producer.buildProperties();
            if (properties != null) {
                result.setProperties(properties);
            } else {
                LOGGER.warn("producerBean warning, properties can't be null");
            }
        } else {
            LOGGER.warn("producerBean warning, producer can't be null");
        }

        return result;
    }

    /**
     * 接收消息
     *
     * @param consumer  接收消息配置
     * @param listeners 监听消息列表
     * @param <T>       {@link RocketMqListener} 类型
     * @return {@link ConsumerBean} 实例
     */
    public <T extends RocketMqListener> ConsumerBean consumerBean(RocketMqProperties.Consumer consumer, List<T> listeners) {
        ConsumerBean result = new ConsumerBean();

        if (consumer != null) {
            Properties properties = consumer.buildProperties();
            if (properties != null) {
                result.setProperties(properties);
            } else {
                LOGGER.warn("consumerBean warning, properties can't be null");
            }
        } else {
            LOGGER.warn("consumerBean warning, consumer can't be null");
        }

        Map<Subscription, MessageListener> subscriptionTable = subscriptionTable(listeners);
        if (subscriptionTable != null) {
            result.setSubscriptionTable(subscriptionTable);
        } else {
            LOGGER.warn("consumerBean warning, subscriptionTable can't be null");
        }

        return result;
    }

    /**
     * 获取订阅列表
     *
     * @param listeners 监听消息列表
     * @param <T>       {@link RocketMqListener} 类型
     * @return 订阅列表
     */
    public <T extends RocketMqListener> Map<Subscription, MessageListener> subscriptionTable(List<T> listeners) {
        if (listeners == null) {
            LOGGER.warn("subscriptionTable warning, listeners can't be null");
            return null;
        }

        Map<Subscription, MessageListener> result = new HashMap<>(64);

        for (RocketMqListener messageListener : listeners) {
            if (messageListener == null) {
                LOGGER.warn("subscriptionTable warning, messageListener can't be null");
                continue;
            }

            Subscription subscription = messageListener.subscription();
            if (subscription == null) {
                LOGGER.warn("subscriptionTable warning, subscription can't be null");
                continue;
            }

            result.put(subscription, messageListener);
        }

        return result;
    }

}
