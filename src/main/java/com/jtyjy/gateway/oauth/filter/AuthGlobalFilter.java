package com.jtyjy.gateway.oauth.filter;

import com.jtyjy.gateway.dto.UserDTO;
import com.jtyjy.gateway.utils.JsonUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 将登录用户的JWT转化成用户信息的全局过滤器
 * 转发出去的header增加json-user属性，存放用户json的信息
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

  private final static Logger LOGGER = LoggerFactory.getLogger(AuthGlobalFilter.class);

  private static final String USER_HEAD = "Authorization";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String urlPath = exchange.getRequest().getPath().value();
    return exchange.getPrincipal().defaultIfEmpty(() -> "unknown").flatMap(principal -> {
      if("unknown".equals(principal.getName())){
        return chain.filter(exchange);
      }
      String authStr = exchange.getRequest().getHeaders().getFirst(USER_HEAD);
      if(StringUtils.isBlank(authStr) || !authStr.startsWith("Bearer")){
        return chain.filter(exchange);
      }
      JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
      Map<String, Object> claims = jwtAuthenticationToken.getToken().getClaims();
      LOGGER.info("AuthGlobalFilter.filter() path:{} user_name:{} nickname:{}", urlPath, claims.get("user_name"), claims.get("nickname"));

      String userJson = JsonUtils.toJson(UserDTO.toUserDTO(claims));
      //将user插入header
      ServerHttpRequest request = exchange.getRequest().mutate()
              .headers(httpHeaders -> httpHeaders.remove(USER_HEAD))
              .header(USER_HEAD, userJson != null ? Base64.encodeBase64String(userJson.getBytes(StandardCharsets.UTF_8)) : "").build();
      ServerWebExchange newExchange = exchange.mutate().request(request).build();

      return chain.filter(newExchange);
    });

  }

  @Override
  public int getOrder() {
    return 1;
  }
}
