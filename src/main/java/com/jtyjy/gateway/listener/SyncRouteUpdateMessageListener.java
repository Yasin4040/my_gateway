package com.jtyjy.gateway.listener;

import com.jtyjy.gateway.manager.MysqlRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class SyncRouteUpdateMessageListener implements MessageListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MysqlRouteDefinitionRepository mysqlRouteDefinitionRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //同步更新所有在线的网关
        String body = new String(message.getBody());
        if("update".equals(body)){
            mysqlRouteDefinitionRepository.loadRouteFromDB();
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
