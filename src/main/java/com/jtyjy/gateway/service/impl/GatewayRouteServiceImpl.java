package com.jtyjy.gateway.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jtyjy.gateway.dto.RouteDTO;
import com.jtyjy.gateway.infrastructure.config.RedisListenerConfig;
import com.jtyjy.gateway.infrastructure.utils.JsonUtils;
import com.jtyjy.gateway.repository.model.GatewayRoute;
import com.jtyjy.gateway.repository.mapper.GatewayRouteMapper;
import com.jtyjy.gateway.service.GatewayRouteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 网关路由表 服务实现类
 * </p>
 *
 * @author zxw
 * @since 2021-09-24
 */
@Service
public class GatewayRouteServiceImpl extends ServiceImpl<GatewayRouteMapper, GatewayRoute> implements GatewayRouteService, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void addRoute(RouteDTO routeDTO) {
        GatewayRoute gatewayRoute = new GatewayRoute();
        gatewayRoute.setServiceId(routeDTO.getServiceId());
        gatewayRoute.setUri(routeDTO.getUri());
        gatewayRoute.setPredicates(JsonUtils.toJson(routeDTO.getPredicates()));
        gatewayRoute.setFilters(JsonUtils.toJson(routeDTO.getFilters()));
        gatewayRoute.setSort(routeDTO.getOrder());
        gatewayRoute.setCreateTime(new Date());
        gatewayRoute.setUpdateTime(new Date());
        save(gatewayRoute);
    }

    @Override
    public List<RouteDTO> routeList() {
        List<GatewayRoute> list = list();
        return list.stream().map(gatewayRoute -> {
            RouteDTO routeDTO = new RouteDTO();
            routeDTO.setUri(gatewayRoute.getUri());
            routeDTO.setOrder(gatewayRoute.getSort());
            routeDTO.setServiceId(gatewayRoute.getServiceId());
            routeDTO.setPredicates(JsonUtils.toObject(gatewayRoute.getPredicates(), new TypeReference<List<String>>() {}));
            routeDTO.setFilters(JsonUtils.toObject(gatewayRoute.getFilters(), new TypeReference<List<String>>() {}));
            return routeDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteRoute(Long id) {
        removeById(id);
    }

    @Override
    public void reloadConfig() {
        redisTemplate.convertAndSend(RedisListenerConfig.SYNC_ROUTE_UPDATE,"update");
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
