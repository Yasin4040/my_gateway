package com.jtyjy.gateway.listener;

import com.jtyjy.gateway.event.DataIpApplicationEvent;
import com.jtyjy.gateway.service.IpBlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Created by ZiYao Lee on 2022/07/07.
 * Time: 10:24
 */
@Slf4j
@Component
public class DataIpApplicationEventListener {

    @Autowired
    private IpBlackService ipBlackService;


    /**
     * 监听事件刷新配置；
     * DataIpApplicationEvent发布后，即触发listenEvent事件方法；
     */
    @EventListener(classes = DataIpApplicationEvent.class)
    public void listenEvent() {
        ipBlackService.refreshIpListNoEvent();
    }

}
