package com.jtyjy.gateway.service;

import com.jtyjy.gateway.dto.RouteDTO;
import com.jtyjy.gateway.repository.model.GatewayRoute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 网关路由表 服务类
 * </p>
 *
 * @author zxw
 * @since 2021-09-24
 */
public interface GatewayRouteService extends IService<GatewayRoute> {

    /**
     * 添加路由规则
     * @param routeDTO
     */
    void addRoute(RouteDTO routeDTO);

    List<RouteDTO> routeList();

    void deleteRoute(Long id);

    void reloadConfig();

}
