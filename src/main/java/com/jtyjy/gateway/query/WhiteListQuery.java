package com.jtyjy.gateway.query;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="WhiteListQuery对象", description="白名单表")
public class WhiteListQuery extends PageQuery {
    @ApiModelProperty(value = "url路径")
    private String path;

    @ApiModelProperty(value = "说明")
    private String description;
}
