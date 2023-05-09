package io.github.changebooks.rocketmq;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * Configuration properties for Rocket Message Queue.
 *
 * @author changebooks@qq.com
 */
@ConfigurationProperties(prefix = "ons.rocket-mq")
public class RocketMqProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqProperties.class);

    /**
     * 发送消息
     */
    public static class Producer {
        /**
         * 域名:端口
         */
        private String bootstrapServers;

        /**
         * 身份
         */
        private String accessKey;

        /**
         * 密钥
         */
        private String secretKey;

        /**
         * 客户端id
         */
        private String groupId;

        /**
         * 构建配置
         *
         * @return {@link Properties} 实例
         */
        public Properties buildProperties() {
            Properties result = new Properties();

            String bootstrapServers = getBootstrapServers();
            if (StringUtils.hasText(bootstrapServers)) {
                result.setProperty(PropertyKeyConst.NAMESRV_ADDR, bootstrapServers);
            } else {
                LOGGER.warn("build producer properties warning, bootstrapServers can't be empty");
            }

            String accessKey = getAccessKey();
            if (StringUtils.hasText(accessKey)) {
                result.setProperty(PropertyKeyConst.AccessKey, accessKey);
            } else {
                LOGGER.warn("build producer properties warning, accessKey can't be empty");
            }

            String secretKey = getSecretKey();
            if (StringUtils.hasLength(secretKey)) {
                result.setProperty(PropertyKeyConst.SecretKey, secretKey);
            } else {
                LOGGER.warn("build producer properties warning, secretKey can't be empty");
            }

            String groupId = getGroupId();
            if (StringUtils.hasText(groupId)) {
                result.setProperty(PropertyKeyConst.GROUP_ID, groupId);
            }

            return result;
        }

        public String getBootstrapServers() {
            return bootstrapServers;
        }

        public void setBootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

    }

    /**
     * 接收消息
     */
    public static class Consumer {
        /**
         * 域名:端口
         */
        private String bootstrapServers;

        /**
         * 身份
         */
        private String accessKey;

        /**
         * 密钥
         */
        private String secretKey;

        /**
         * 客户端id
         */
        private String groupId;

        /**
         * 消费线程数
         */
        private Integer threadNum;

        /**
         * 客户端缓存，最大消息数
         * 默认：5000
         * 范围：[100, 50000]
         */
        private Integer maxCachedAmount;

        /**
         * 批量消息，最大消息数
         * 默认：1
         * 范围：[1, 1024]
         */
        private Integer maxBatchSize;

        /**
         * 批量消息，客户端聚合消息，最长耗时
         * 默认：0，即：从服务端取到消息后立即消费
         * 最大：ConsumeTimeout / 2
         */
        private Integer maxBatchAwaitSecond;

        /**
         * 构建配置
         *
         * @return {@link Properties} 实例
         */
        public Properties buildProperties() {
            Properties result = new Properties();

            String bootstrapServers = getBootstrapServers();
            if (StringUtils.hasText(bootstrapServers)) {
                result.setProperty(PropertyKeyConst.NAMESRV_ADDR, bootstrapServers);
            } else {
                LOGGER.warn("build consumer properties warning, bootstrapServers can't be empty");
            }

            String accessKey = getAccessKey();
            if (StringUtils.hasText(accessKey)) {
                result.setProperty(PropertyKeyConst.AccessKey, accessKey);
            } else {
                LOGGER.warn("build consumer properties warning, accessKey can't be empty");
            }

            String secretKey = getSecretKey();
            if (StringUtils.hasLength(secretKey)) {
                result.setProperty(PropertyKeyConst.SecretKey, secretKey);
            } else {
                LOGGER.warn("build consumer properties warning, secretKey can't be empty");
            }

            String groupId = getGroupId();
            if (StringUtils.hasText(groupId)) {
                result.setProperty(PropertyKeyConst.GROUP_ID, groupId);
            } else {
                LOGGER.warn("build consumer properties warning, groupId can't be empty");
            }

            Integer threadNum = getThreadNum();
            if (threadNum != null && threadNum > 0) {
                result.setProperty(PropertyKeyConst.ConsumeThreadNums, String.valueOf(threadNum));
            }

            Integer maxCachedAmount = getMaxCachedAmount();
            if (maxCachedAmount != null && maxCachedAmount > 0) {
                result.setProperty(PropertyKeyConst.MaxCachedMessageAmount, String.valueOf(maxCachedAmount));
            }

            Integer maxBatchSize = getMaxBatchSize();
            if (maxBatchSize != null && maxBatchSize > 0) {
                result.setProperty(PropertyKeyConst.ConsumeMessageBatchMaxSize, String.valueOf(maxBatchSize));
            }

            Integer maxBatchAwaitSecond = getMaxBatchAwaitSecond();
            if (maxBatchAwaitSecond != null && maxBatchAwaitSecond > 0) {
                result.setProperty(PropertyKeyConst.BatchConsumeMaxAwaitDurationInSeconds, String.valueOf(maxBatchAwaitSecond));
            }

            return result;
        }

        public String getBootstrapServers() {
            return bootstrapServers;
        }

        public void setBootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public Integer getThreadNum() {
            return threadNum;
        }

        public void setThreadNum(Integer threadNum) {
            this.threadNum = threadNum;
        }

        public Integer getMaxCachedAmount() {
            return maxCachedAmount;
        }

        public void setMaxCachedAmount(Integer maxCachedAmount) {
            this.maxCachedAmount = maxCachedAmount;
        }

        public Integer getMaxBatchSize() {
            return maxBatchSize;
        }

        public void setMaxBatchSize(Integer maxBatchSize) {
            this.maxBatchSize = maxBatchSize;
        }

        public Integer getMaxBatchAwaitSecond() {
            return maxBatchAwaitSecond;
        }

        public void setMaxBatchAwaitSecond(Integer maxBatchAwaitSecond) {
            this.maxBatchAwaitSecond = maxBatchAwaitSecond;
        }

    }

    /**
     * 发送消息
     */
    private Producer producer;

    /**
     * 接收消息
     */
    private Consumer consumer;

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

}
