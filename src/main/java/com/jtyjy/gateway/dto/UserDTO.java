package com.jtyjy.gateway.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.minidev.json.JSONArray;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class UserDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户账号")
    private String account;

    /**
     * 用户类型
     * 企业用户：1
     * 普通用户：2
     */
    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("用户可请求资源范围")
    private List<String> scopes;

    @ApiModelProperty("用户拥有的权限")
    private List<String> authorities;

    @ApiModelProperty("oauth原始token")
    private String token;

    public static UserDTO toUserDTO(Map<String, Object> claims){
        UserDTO userDTO = new UserDTO();
        userDTO.setId((Long) claims.get("id"));
        userDTO.setAccount((String) claims.get("user_name"));
        userDTO.setUserType(claims.get("userType") != null ? ((Long) claims.get("userType")).intValue():null);
        userDTO.setToken((String) claims.get("token"));
        JSONArray scopes = (JSONArray) claims.get("scope");
        JSONArray authorities = (JSONArray) claims.get("authorities");
        if(scopes != null) {
            userDTO.setScopes(Stream.of(scopes.toArray()).map(s -> (String) s).collect(Collectors.toList()));
        }
        if(authorities != null) {
            userDTO.setAuthorities(Stream.of(authorities.toArray()).map(s -> (String) s).collect(Collectors.toList()));
        }
        return userDTO;
    }

}
