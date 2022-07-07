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
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.ArrayList;

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
    public Mono<Result<PageBody>> getAllInterface(@RequestParam(name = "serviceUrl") String serviceUrl,
                                                  @RequestParam(name = "path") String path,


                                                  @RequestParam(name = "summary")  String summary){


        PageBody<InterfaceDTO> result= new PageBody<InterfaceDTO>(new ArrayList<>());
        return loadBalanceWebClientBuilder
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(10 * 1024 * 1024))
                        .build())
                .baseUrl(serviceUrl+"/v3/api-docs")
                .build()
                .get().retrieve()
                .bodyToMono(String.class)

                .map(x->Result.ok(new PageBody(
                        gatewayRouteService.mapToInterfaceDTO(x,path,summary)
                               )
                ))
               .onErrorReturn(new Result("500","获取不到实例",result));

    }


//    @GetMapping("/testFlux")
//    @ApiOperation(value = "testFlux")
//    public Mono<Result<PageBody<InterfaceDTO>>> testFluxInner(Long id){
//        return loadBalanceWebClientBuilder
//                .exchangeStrategies(ExchangeStrategies.builder()
//                        .codecs(configurer -> configurer
//                                .defaultCodecs()
//                                .maxInMemorySize(10 * 1024 * 1024))
//                 .build())
//                .baseUrl("http://JTYJY-API-GATEWAY/v3/api-docs")
//                .build()
//                .get().retrieve()
//                .bodyToMono(String.class)
//                .map(x->Result.ok(new PageBody(gatewayRouteService.mapToInterfaceDTO(x))));
//
//    }
}

