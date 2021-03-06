package com.jtyjy.gateway.service.impl;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jtyjy.gateway.config.RedisListenerConfig;
import com.jtyjy.gateway.constants.RedisTypeConstants;
import com.jtyjy.gateway.converter.RouteConverter;
import com.jtyjy.gateway.dto.InterfaceDTO;
import com.jtyjy.gateway.dto.RouteDTO;
import com.jtyjy.gateway.query.RouteQuery;
import com.jtyjy.gateway.repository.mapper.GatewayRouteMapper;
import com.jtyjy.gateway.repository.model.GatewayRoute;
import com.jtyjy.gateway.service.GatewayRouteService;
import com.jtyjy.gateway.utils.JsonUtils;
import com.jtyjy.gateway.vo.RouteVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
    @Autowired
    private RestTemplate restTemplate;


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
            routeDTO.setPredicates(JsonUtils.toObject(gatewayRoute.getPredicates(), new TypeReference<List<String>>() {
            }));
            routeDTO.setFilters(JsonUtils.toObject(gatewayRoute.getFilters(), new TypeReference<List<String>>() {
            }));
            return routeDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteRoute(Long id) {
        removeById(id);
    }

    @Override
    public void reloadConfig() {
        redisTemplate.convertAndSend(RedisListenerConfig.SYNC_ROUTE_UPDATE, RedisTypeConstants.ROUTE_UPDATE);
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    @Override
    public Page<RouteVO> selectRoutePageVo(RouteQuery query) {

        Page<GatewayRoute> page = this.page(new Page<>(query.getPageNum(), query.getPageSize()), new LambdaQueryWrapper<GatewayRoute>()
                .like(StringUtils.isNotBlank(query.getServiceId()), GatewayRoute::getServiceId, query.getServiceId())
                .like(StringUtils.isNotBlank(query.getPredicates()), GatewayRoute::getPredicates, query.getPredicates())
                .like(StringUtils.isNotBlank(query.getUri()), GatewayRoute::getUri, query.getUri())
                .like(StringUtils.isNotBlank(query.getRemarks()), GatewayRoute::getRemarks, query.getRemarks())
        );
        return RouteConverter.INSTANCE.toPageRouteVO(page);
    }


    @Override
    public  List<InterfaceDTO> mapToInterfaceDTO(String result,String path,String summary){
        List<InterfaceDTO> faces = new ArrayList<>();
        String paths = getJsonValue(result, "paths");
        if(StringUtils.isBlank(paths) ){
            return null;
        }
        Set<String> pathSet = getKeySet(paths);
        //过滤 路径path 搜索
        if(StringUtils.isNotBlank(path)){
            pathSet =  pathSet.stream().filter(x->x.contains(path)).collect(Collectors.toSet());
        }
        for (String p : pathSet) {
            InterfaceDTO dto = new InterfaceDTO();
            dto.setPath(p);

            //当前对象
            //当前key对应的值
            String pathValue = getJsonValue(paths, p);

            String type = getKeySet(pathValue).stream().findFirst().get();

            dto.setType(type);

            String typeValue = getJsonValue(pathValue, type);
            String tag = getJsonValue(typeValue, "tags");
            dto.setTag(tag);
            String summaryResult = getJsonValue(typeValue, "summary");
            //过滤 summary 路径说明 搜索
            if(StringUtils.isNotBlank(summary)&& !summaryResult.contains(summary)){
               break;
            }
            dto.setSummary(summaryResult);
            faces.add(dto);
        }
        return faces;
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public String getJsonValue(String data, String key) {
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.getString(key);
    }

    public Set<String> getKeySet(String data) {
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.keySet();
    }

}
