package com.jtyjy.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.jtyjy.basic.common.web.Result;
import com.jtyjy.gateway.service.IpBlackService;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Description:
 * Created by ZiYao Lee on 2022/07/04.
 * Time: 14:43
 */

@RestController
@RequestMapping("/gateway/ipBlack")
public class IpBlackController {

    @Autowired
    private IpBlackService ipBlackService;


    @GetMapping("/getIpBlacklist")
    @ApiOperation(value = "获取Ip黑名单")
    public Mono<Result> getIpBlacklist() {
        return ipBlackService.getIpList();
    }


    @PostMapping("/addIp")
    @ApiOperation(value = "添加Ip黑名单")
    public Mono<Result> addIp(@RequestBody JSONObject object) {
        return ipBlackService.addIp(object.getString("ip"), object.getString("remark"));
    }

    @PostMapping("/delIp")
    @ApiOperation(value = "删除Ip黑名单")
    public Mono<Result> delIp(String ip) {
        return ipBlackService.delIp(ip);
    }
}