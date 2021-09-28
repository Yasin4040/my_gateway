package com.jtyjy.gateway.oauth.filter;

import com.jtyjy.gateway.dto.UserDTO;
import com.jtyjy.gateway.infrastructure.utils.JsonUtils;
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

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    return exchange.getPrincipal().flatMap(principal -> {
      JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
      Map<String, Object> claims = jwtAuthenticationToken.getToken().getClaims();
      LOGGER.info("AuthGlobalFilter.filter() user:{}", claims);

      //将user插入header
      UserDTO userDTO = new UserDTO();
      userDTO.setId((Long) claims.get("id"));
      userDTO.setUsername((String) claims.get("user_name"));
      JSONArray scopes = (JSONArray) claims.get("scope");
      JSONArray authorities = (JSONArray) claims.get("authorities");
      userDTO.setScopes(Stream.of(scopes.toArray()).map(s->(String)s).collect(Collectors.toList()));
      userDTO.setAuthorities(Stream.of(authorities.toArray()).map(s->(String)s).collect(Collectors.toList()));
      ServerHttpRequest request = exchange.getRequest().mutate()
              .headers(httpHeaders -> httpHeaders.remove("Authorization"))
              .header("json-user", JsonUtils.toJson(userDTO)).build();
      ServerWebExchange newExchange = exchange.mutate().request(request).build();

      return chain.filter(newExchange);
    });

  }

  @Override
  public int getOrder() {
    return 0;
  }
}
