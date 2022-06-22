package com.jtyjy.gateway.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value="Route对象", description="网关路由表")
public class RouteDTO {

    @ApiModelProperty(value = "服务id", required = true)
    @NotNull
    private String serviceId;

    @ApiModelProperty(value = "转发地址", required = true)
    @NotNull
    private String uri;

    @ApiModelProperty(value = "访问路径")
    private List<String> predicates;

    @ApiModelProperty(value = "过滤")
    private List<String> filters;

    @ApiModelProperty(value = "排序")
    private int order;

    @ApiModelProperty(value = "备注信息")
    private String remarks;

}
