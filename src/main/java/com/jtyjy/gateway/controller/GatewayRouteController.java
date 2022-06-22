package com.jtyjy.gateway.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtyjy.gateway.dto.PageBody;
import com.jtyjy.gateway.dto.RouteDTO;
import com.jtyjy.gateway.query.RouteQuery;
import com.jtyjy.gateway.vo.RouteVO;
import com.jtyjy.basic.common.web.Result;
import com.jtyjy.gateway.service.GatewayRouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

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
    private GatewayRouteService gatewayRouteService;

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

}

