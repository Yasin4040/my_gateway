package com.jtyjy.gateway.repository.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName gw_service_interface
 */
@TableName(value ="gw_service_interface")
@Data
public class ServiceInterface implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务id
     */
    @TableField(value = "service_id")
    private String serviceId;

    /**
     * 服务名称 对应nacos
     */
    @TableField(value = "service_url")
    private String serviceUrl;

    /**
     * 接口路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 请求路径名称
     */
    @TableField(value = "summary")
    private String summary;

    /**
     * 请求HTTP
     */
    @TableField(value = "type")
    private String type;

    /**
     * 请求分组
     */
    @TableField(value = "tag")
    private String tag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}