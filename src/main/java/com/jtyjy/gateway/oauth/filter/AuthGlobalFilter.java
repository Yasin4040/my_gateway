package com.jtyjy.gateway.oauth.filter;

import com.jtyjy.gateway.dto.UserDTO;
import com.jtyjy.gateway.utils.JsonUtils;
import net.minidev.json.JSONArray;
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

  public static final String userHead = "json-user";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    return exchange.getPrincipal().defaultIfEmpty(() -> "unknown").flatMap(principal -> {
      if("unknown".equals(principal.getName())){
        return chain.filter(exchange);
      }
      JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
      Map<String, Object> claims = jwtAuthenticationToken.getToken().getClaims();
      LOGGER.info("AuthGlobalFilter.filter() user:{}", claims);

      //将user插入header
      ServerHttpRequest request = exchange.getRequest().mutate()
              .headers(httpHeaders -> httpHeaders.remove("Authorization"))
              .header(userHead, JsonUtils.toJson(UserDTO.toUserDTO(claims))).build();
      ServerWebExchange newExchange = exchange.mutate().request(request).build();

      return chain.filter(newExchange);
    });

  }

  @Override
  public int getOrder() {
    return 1;
  }
}
