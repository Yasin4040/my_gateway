package com.jtyjy.gateway.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="IpBlackVO对象", description="ip 黑名单表")
public class IpBlackVO {
    @ApiModelProperty(value = "ip")
    private String ip;
    @ApiModelProperty(value = "备注信息")
    private String remark;
}
