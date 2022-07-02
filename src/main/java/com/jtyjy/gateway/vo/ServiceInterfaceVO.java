package com.jtyjy.gateway.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ServiceInterface对象", description="ServiceInterface表")
public class ServiceInterfaceVO {

    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 服务id
     */
    @ApiModelProperty(value = "服务id")
    private String serviceId;

    /**
     * 服务名称 对应nacos
     */
    @ApiModelProperty(value = "服务名称 对应nacos")
    private String serviceUrl;

    /**
     * 接口路径
     */
    @ApiModelProperty(value = "接口路径")
    private String path;

    /**
     * 请求路径名称
     */
    @ApiModelProperty(value = "请求路径名称")
    private String summary;

    /**
     * 请求HTTP
     */
    @ApiModelProperty(value = "请求HTTP")
    private String type;

    /**
     * 请求分组
     */
    @ApiModelProperty(value = "请求分组")
    private String tag;
}
