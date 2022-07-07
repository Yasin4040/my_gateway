package com.jtyjy.gateway.event;

import org.springframework.context.ApplicationEvent;

/**
 * Description: 创建自定义IP事件
 * Created by ZiYao Lee on 2022/07/07.
 * Time: 10:23
 */
public class DataIpApplicationEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DataIpApplicationEvent(Object source){
        super(source);
    }
}
