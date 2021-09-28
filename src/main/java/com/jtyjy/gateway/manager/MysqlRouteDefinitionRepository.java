package com.jtyjy.gateway.manager;

import com.jtyjy.gateway.dto.RouteDTO;
import com.jtyjy.gateway.infrastructure.utils.JsonUtils;
import com.jtyjy.gateway.repository.model.GatewayRoute;
import com.jtyjy.gateway.service.GatewayRouteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 核心配置类，加载数据库的路由配置信息到数据库
 */
@Component
@Slf4j
public class MysqlRouteDefinitionRepository implements RouteDefinitionRepository {

    @Autowired
    private GatewayRouteService gatewayRouteService;

    private List<RouteDefinition> routeDefinitions = new ArrayList<>();

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        /*List<RouteDTO> routeDTOList = gatewayRouteService.routeList();
        List<RouteDefinition> routeDefinitions = routeDTOList.stream().map(routeDTO -> {
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.setId(routeDTO.getServiceId());
            routeDefinition.setUri(URI.create(routeDTO.getUri()));
            routeDefinition.setOrder(routeDTO.getOrder());
            routeDefinition.setPredicates(routeDTO.getPredicates().stream().map(PredicateDefinition::new).collect(Collectors.toList()));
            routeDefinition.setFilters(routeDTO.getFilters().stream().map(FilterDefinition::new).collect(Collectors.toList()));
            return routeDefinition;
        }).collect(Collectors.toList());*/
        if(CollectionUtils.isEmpty(routeDefinitions)){
            loadRouteFromDB();
        }
        //log.info("更新RouteDefinitions");
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return Mono.defer(() -> Mono.error(new NotFoundException("Unsupported operation")));
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return Mono.defer(() -> Mono.error(new NotFoundException("Unsupported operation")));
    }

    public void loadRouteFromDB(){
        log.info("从数据库更新RouteDefinitions");
        List<RouteDTO> routeDTOList = gatewayRouteService.routeList();
        routeDefinitions = routeDTOList.stream().map(routeDTO -> {
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.setId(routeDTO.getServiceId());
            routeDefinition.setUri(URI.create(routeDTO.getUri()));
            routeDefinition.setOrder(routeDTO.getOrder());
            routeDefinition.setPredicates(routeDTO.getPredicates().stream().map(PredicateDefinition::new).collect(Collectors.toList()));
            routeDefinition.setFilters(routeDTO.getFilters().stream().map(FilterDefinition::new).collect(Collectors.toList()));
            return routeDefinition;
        }).collect(Collectors.toList());
    }
}