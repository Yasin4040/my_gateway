package com.jtyjy.gateway.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtyjy.basic.common.web.Result;
import com.jtyjy.gateway.dto.InterfaceDTO;
import com.jtyjy.gateway.dto.PageBody;
import com.jtyjy.gateway.dto.RouteDTO;
import com.jtyjy.gateway.query.RouteQuery;
import com.jtyjy.gateway.service.GatewayRouteService;
import com.jtyjy.gateway.vo.RouteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

/**
 * <p>
 * 网关路由表 前端控制器
 * </p>
 *
 * @author zxw
 * @since 2021-09-24
 */
@RestController
@RequestMapping("/gateway/route")
@Api(tags = "路由管理")
public class GatewayRouteController {
    @Autowired
    private LoadBalancerClientFactory loadBalancerClientFactory;


    @Autowired
    private GatewayRouteService gatewayRouteService;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient.Builder loadBalanceWebClientBuilder;

    @GetMapping("/selectRoutePageVo")
    @ApiOperation(value = "分页查询路由")
    public Result<PageBody<RouteVO>> selectRoutePageVo(@Validated @ModelAttribute RouteQuery query){
        Page<RouteVO> routePage = gatewayRouteService.selectRoutePageVo(query);
        return Result.ok(new PageBody(routePage));
    }

    @PostMapping("/addRoute")
    @ApiOperation(value = "添加路由")
    public Result<Void> addRoute(@Validated @RequestBody RouteDTO routeDTO){
        gatewayRouteService.addRoute(routeDTO);
        return Result.ok();
    }

    @PostMapping("/deleteRoute")
    @ApiOperation(value = "删除路由")
    public Result<Void> deleteRoute(Long id){
        gatewayRouteService.deleteRoute(id);
        return Result.ok();
    }

    @PostMapping("/reloadConfig")
    @ApiOperation(value = "刷新配置")
    public Result<Void> reloadConfig(@ApiIgnore Principal principal){
        gatewayRouteService.reloadConfig();
        return Result.ok();
    }

    @GetMapping("/getAllInterface")
    @ApiOperation(value = "获取所有接口")
    public Result<List<InterfaceDTO>> getAllInterface(Long id){
        return Result.ok(gatewayRouteService.getAllInterface(id));
//        return Result.ok(list);
    }


//    @GetMapping("/testFluxInner")
//    @ApiOperation(value = "testFluxInner")
    public Flux<Map> testFluxInner(){
        return loadBalanceWebClientBuilder
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(10 * 1024 * 1024))
                 .build())
                .baseUrl("http://JTYJY-API-GATEWAY/v3/api-docs")
                .build()
                .get().retrieve().bodyToFlux(Map.class);
    }


    @GetMapping("/testFlux")
    @ApiOperation(value = "testFlux")
    public String testFlux2(){
        String forObject = restTemplate.getForObject("http://JTYJY-API-GATEWAY/v3/api-docs", String.class);

        System.out.println(forObject);
//        boolean lb = forObject.contains("lb");
        System.out.println("aaa");
        return forObject;
    }

}

