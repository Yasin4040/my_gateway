package com.jtyjy.gateway.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class RedisListenerConfig {

    public static final String SYNC_ROUTE_UPDATE = "sync:route:update";

    //redis连接工厂
    @Autowired
    private RedisConnectionFactory connectionFactory;

    //redis 消息监听器
    @Autowired
    private MessageListener messageListener;

    /**
     *创建任务池,运行线程等待处理redis消息
     */
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        return taskScheduler;
    }

    /**
     *定义Redis的监听容器
     */
    @Bean
    public RedisMessageListenerContainer initRedisContainer(){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        //redis 连接工厂
        container.setConnectionFactory(connectionFactory);
        //设置运行任务池
        container.setTaskExecutor(threadPoolTaskScheduler());
        //定义监听渠道
        Topic topic = new ChannelTopic(SYNC_ROUTE_UPDATE);
        //定义监听器监听的Redis的消息
        container.addMessageListener(messageListener, topic);
        return container;
    }

}
