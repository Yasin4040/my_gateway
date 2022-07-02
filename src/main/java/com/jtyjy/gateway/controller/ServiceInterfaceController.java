package com.jtyjy.gateway.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtyjy.basic.common.web.Result;
import com.jtyjy.gateway.dto.InterfaceDTO;
import com.jtyjy.gateway.dto.PageBody;
import com.jtyjy.gateway.dto.RouteDTO;
import com.jtyjy.gateway.query.RouteQuery;
import com.jtyjy.gateway.query.ServiceInterfaceQuery;
import com.jtyjy.gateway.service.GatewayRouteService;
import com.jtyjy.gateway.service.ServiceInterfaceService;
import com.jtyjy.gateway.vo.RouteVO;
import com.jtyjy.gateway.vo.ServiceInterfaceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * 网关路由表 前端控制器
 * </p>
 *
 * @author zxw
 * @since 2021-09-24
 */
@RestController
@RequestMapping("/gateway/interface")
@Api(tags = "路由管理")
public class ServiceInterfaceController {

    @Autowired
    private ServiceInterfaceService service;

    @Autowired
    private GatewayRouteService gatewayRouteService;


    @GetMapping("/selectServiceInterfacePageVo")
    @ApiOperation(value = "分页查询路由")
    public Result<PageBody<ServiceInterfaceVO>> selectServiceInterfacePageVo(@Validated @ModelAttribute ServiceInterfaceQuery query){
        new Thread(
                ()->gatewayRouteService.dealInterface(query.getId()))
                .start();
        Page<ServiceInterfaceVO> routePage = service.selectServiceInterfacePageVo(query);
        return Result.ok(new PageBody(routePage));
    }

}

