package com.jtyjy.gateway.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GatewayRoute对象", description="网关路由表")
public class RouteVO {
    private Long id;
    @ApiModelProperty(value = "服务id")
    private String serviceId;

    @ApiModelProperty(value = "转发地址")
    private String uri;

    @ApiModelProperty(value = "访问路径")
    private String predicates;

    @ApiModelProperty(value = "过滤")
    private String filters;

    @ApiModelProperty(value = "顺序")
    private Integer sort;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    @ApiModelProperty(value = "备注信息")
    private String remarks;
}
