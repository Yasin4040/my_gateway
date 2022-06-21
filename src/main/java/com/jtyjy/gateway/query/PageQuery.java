package com.jtyjy.gateway.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Created by ZiYao Lee on 2022/06/20.
 * Time: 16:52
 */
@Data
public class PageQuery {
    @ApiModelProperty("当前页码")
    private Integer pageNum = 1;
    @ApiModelProperty("每页条数")
    private Integer pageSize = 20;
}
