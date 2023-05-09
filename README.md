# changebook-rocketmq
### RocketMQ

### pom.xml
```
<dependency>
  <groupId>io.github.changebooks</groupId>
  <artifactId>changebook-rocketmq</artifactId>
  <version>1.1.2</version>
</dependency>
```

### 拦截发送消息：从日志上下文获取日志信息，设置待发送消息
```
SPI接口
resources
  META-INF
    services
      com.aliyun.openservices.ons.api.spi.ProducerInterceptor

SPI实现
io.github.changebooks.rocketmq.LogProducerInterceptor
```

### 拦截接收消息：从已接收消息获取日志信息，设置日志上下文
```
SPI接口
resources
  META-INF
    services
      com.aliyun.openservices.ons.api.spi.ConsumerInterceptor

SPI实现
io.github.changebooks.rocketmq.LogConsumerInterceptor
```

### application.yml
```
ons:
  rocket-mq:
    producer:
      bootstrap-servers: 
      access-key: 
      secret-key: 
      group-id: 
    consumer:
      bootstrap-servers: ${ons.rocket-mq.producer.bootstrap-servers}
      access-key: ${ons.rocket-mq.producer.access-key}
      secret-key: ${ons.rocket-mq.producer.secret-key}
      group-id: ${ons.rocket-mq.producer.group-id}
      thread-num: 1
      max-cached-amount: 5000
      max-batch-size: 1
      max-batch-await-second: 0
```

### 实现 RocketMqSupport
```
@Configuration
@EnableConfigurationProperties({RocketMqProperties.class})
public class RocketMqSupportImpl extends RocketMqSupport {

    public static final String TOPIC = "";
    public static final String TAG = "";

    @Resource
    private RocketMqProperties rocketMqProperties;

    @Resource
    private List<Topic001MsgListener> topic001MsgListeners;

    @Bean(name = "topic001Producer", initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean topic001Producer() {
        return super.producerBean(rocketMqProperties.getProducer());
    }

    @Bean(name = "topic001Consumer", initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean topic001Consumer() {
        return super.consumerBean(rocketMqProperties.getConsumer(), topic001MsgListeners);
    }

}
```

### 发送消息
```
@Service
public class Topic001MsgSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Topic001MsgSender.class);

    @Resource
    private ProducerBean topic001Producer;

    /**
     * 发送消息
     *
     * @param value 消息内容
     */
    public void send(String value) {
        Message message = new Message();
        message.setTopic(RocketMqSupportImpl.TOPIC);
        message.setTag(RocketMqSupportImpl.TAG);
        message.setBody(value.getBytes());

        SendResult sendResult = topic001Producer.send(message);
        LOGGER.info("send trace, value: {}, thread: {}, sendResult: {}", value, Thread.currentThread().getId(), sendResult);
    }

}
```

### 接收消息
```
@Service
public class Topic001MsgListener implements RocketMqListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Topic001MsgListener.class);

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        LOGGER.info("consume, message: {}", new String(message.getBody(), StandardCharsets.UTF_8));
        return Action.CommitMessage;
    }

    @Override
    public Subscription subscription() {
        Subscription subscription = new Subscription();

        subscription.setTopic(RocketMqSupportImpl.TOPIC);
        subscription.setExpression(RocketMqSupportImpl.TAG);

        return subscription;
    }

}
```
