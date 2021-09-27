package com.jtyjy.gateway.repository.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 网关路由表
 * </p>
 *
 * @author zxw
 * @since 2021-09-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gw_gateway_route")
@ApiModel(value="GatewayRoute对象", description="网关路由表")
public class GatewayRoute implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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
