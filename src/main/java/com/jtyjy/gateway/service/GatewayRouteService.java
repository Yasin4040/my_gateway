package com.jtyjy.gateway.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtyjy.gateway.dto.InterfaceDTO;
import com.jtyjy.gateway.dto.RouteDTO;
import com.jtyjy.gateway.query.RouteQuery;
import com.jtyjy.gateway.repository.model.GatewayRoute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jtyjy.gateway.vo.RouteVO;
import com.jtyjy.gateway.web.Result;
import reactor.core.publisher.Flux;

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

    Page<RouteVO> selectRoutePageVo(RouteQuery query);

    List<InterfaceDTO> getAllInterface(Long id);

    void dealInterface(Long id);
}
