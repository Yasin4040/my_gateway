package com.jtyjy.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Description:
 * Created by ZiYao Lee on 2022/07/02.
 * Time: 16:00
 */
@Configuration
@Order(-1)
public class LoadBalancerConfig {
    @Autowired
    private LoadBalancerClientFactory loadBalancerClientFactory;

    @Autowired
    private LoadBalancerProperties properties;


    @Bean
    public LoadBalancerClient blockingLoadBalancerClient() {
        return new CustomBlockingLoadBalancerClient(loadBalancerClientFactory,properties);
    }
}
