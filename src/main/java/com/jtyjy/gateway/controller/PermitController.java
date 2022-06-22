package com.jtyjy.gateway.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtyjy.gateway.dto.PageBody;
import com.jtyjy.gateway.query.WhiteListQuery;
import com.jtyjy.gateway.repository.model.WhiteList;
import com.jtyjy.gateway.service.WhiteListService;
import com.jtyjy.gateway.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zxw
 * @date 2022/3/15 15:45
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gateway/permit")
@Api(tags = "白名单管理 无需认证的工具 ")
public class PermitController {

    private final WhiteListService whiteListService;
    @GetMapping("/selectWhiteListPageVo")
    @ApiOperation(value = "分页查询白名单")
    public com.jtyjy.basic.common.web.Result<PageBody<WhiteList>> selectWhiteListPageVo(@Validated @ModelAttribute WhiteListQuery query){
        Page<WhiteList> whiteListPage = whiteListService.selectWhiteListPageVo(query);
        return  com.jtyjy.basic.common.web.Result.ok(new PageBody(whiteListPage));
    }

    @GetMapping("/getWhiteList")
    @ApiOperation(value = "获取白名单")
    public Result<List<WhiteList>> getWhiteList(){
        return Result.ok(whiteListService.list());
    }

    @PostMapping("/addWhiteList")
    @ApiOperation(value = "添加白名单")
    public Result<Void> addWhiteList(WhiteList whiteList){
        whiteListService.save(whiteList);
        return Result.ok();
    }

    @PostMapping("/delWhiteList")
    @ApiOperation(value = "删除白名单")
    public Result<Void> delWhiteList(String path){
        Map<String, Object> map = new HashMap<>();
        map.put("path", path);
        whiteListService.removeByMap(map);
        return Result.ok();
    }

    @PostMapping("/refreshWhiteList")
    @ApiOperation(value = "刷新白名单")
    public Result<Void> refreshWhiteList(){
        whiteListService.refreshPathList();
        return Result.ok();
    }

}
