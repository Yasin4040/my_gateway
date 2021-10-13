package com.jtyjy.gateway.controller;

import com.jtyjy.gateway.dto.UserDTO;
import com.jtyjy.gateway.web.Result;
import io.swagger.annotations.Api;
import net.minidev.json.JSONArray;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zxw
 * @date 2021/10/12 18:24
 */
@RestController
@RequestMapping("/gateway/tools")
@Api(tags = "工具")
public class ToolsController {

    @GetMapping("/getUserJson")
    public Result<UserDTO> getUserJson(@ApiIgnore Principal principal){
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        Map<String, Object> claims = jwtAuthenticationToken.getToken().getClaims();
        return Result.ok(UserDTO.toUserDTO(claims));
    }

}
