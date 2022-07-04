package com.jtyjy.gateway.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Created by ZiYao Lee on 2022/06/30.
 * Time: 16:29
 */
@Data
public class InterfaceDTO {
//    @ApiModelProperty(name = "服务名")
//    private String serviceId;
//    @ApiModelProperty(name = "服务名url lb:")
//    private String serviceUrl;
    @ApiModelProperty(name = "请求路径")
    private String path;
    @ApiModelProperty(name = "请求名称")
    private String summary;
    @ApiModelProperty(name = "http类型")
    private String type;
    @ApiModelProperty(name = "标志分组")
    private String tag;
}
