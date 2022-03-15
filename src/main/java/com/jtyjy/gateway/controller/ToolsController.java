package com.jtyjy.gateway.controller;

import com.jtyjy.gateway.dto.UserDTO;
import com.jtyjy.gateway.repository.model.WhiteList;
import com.jtyjy.gateway.service.WhiteListService;
import com.jtyjy.gateway.utils.JsonUtils;
import com.jtyjy.gateway.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zxw
 * @date 2021/10/12 18:24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gateway/tools")
@Api(tags = "工具")
public class ToolsController {

    @GetMapping("/getUserJson")
    public Result<Map<String, Object>> getUserJson(@ApiIgnore Principal principal){
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        Map<String, Object> claims = jwtAuthenticationToken.getToken().getClaims();
        Map<String, Object> map = new HashMap<>();
        UserDTO user = UserDTO.toUserDTO(claims);
        map.put("user", user);
        map.put("base64", Base64.encodeBase64String(JsonUtils.toJson(user).getBytes(StandardCharsets.UTF_8)));
        return Result.ok(map);
    }

}
