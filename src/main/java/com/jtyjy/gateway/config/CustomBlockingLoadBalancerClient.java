package com.jtyjy.gateway.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Description:
 * Created by ZiYao Lee on 2022/07/02.
 * Time: 14:58
 */
public class CustomBlockingLoadBalancerClient extends BlockingLoadBalancerClient {
    private final ReactiveLoadBalancer.Factory<ServiceInstance> loadBalancerClientFactory;


    public CustomBlockingLoadBalancerClient(LoadBalancerClientFactory loadBalancerClientFactory,
                                            LoadBalancerProperties properties) {
        super(loadBalancerClientFactory, properties);
        this.loadBalancerClientFactory = loadBalancerClientFactory;
    }

    @Override
    public <T> ServiceInstance choose(String serviceId, Request<T> request) {
        ReactiveLoadBalancer<ServiceInstance> loadBalancer = loadBalancerClientFactory.getInstance(serviceId);
        if (loadBalancer == null) {
            return null;
        }
        CompletableFuture<Response<ServiceInstance>> f = CompletableFuture.supplyAsync(() -> {
            Response<ServiceInstance> loadBalancerResponse = Mono.from(loadBalancer.choose(request)).block();
            return loadBalancerResponse;
        });
        Response<ServiceInstance> loadBalancerResponse = null;
        try {
            loadBalancerResponse = f.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (loadBalancerResponse == null) {
            return null;
        }
        return loadBalancerResponse.getServer();
    }

}
