package com.jtyjy.gateway.repository.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zxw
 * @since 2022-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gw_white_list")
@ApiModel(value="WhiteList对象", description="")
public class WhiteList implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "url路径")
    private String path;

    @ApiModelProperty(value = "说明")
    private String description;


}
