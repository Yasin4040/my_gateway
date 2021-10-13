package com.jtyjy.gateway.oauth.config;

import com.jtyjy.gateway.oauth.handler.RestAuthenticationEntryPoint;
import com.jtyjy.gateway.oauth.handler.RestfulAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 授权配置
 */
@EnableWebFluxSecurity
@Configuration
public class ResourceSecurityConfig {

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.authorizeExchange()
                // 允许actuator endpoints不进行认证
                .matchers(EndpointRequest.toAnyEndpoint()).permitAll()
                // SCOPE_ 前缀对应认证服务器的客户端 scopes(...) 配置
                //.pathMatchers("/api").hasAuthority("SCOPE_api")
                .pathMatchers("/gateway/route/**").permitAll()
                .pathMatchers("/uaa/**").permitAll()
                .pathMatchers("/v2/**", "/v3/**", "/swagger-resources/**", "/doc.html", "/webjars/**").permitAll()
                //.pathMatchers("/*.js").authenticated()
                //.anyExchange().permitAll()
                .anyExchange().authenticated()
                //.anyExchange().access(authorizationManager) // 鉴权管理器配置
                .and().exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler) // 处理未授权
                .authenticationEntryPoint(restAuthenticationEntryPoint) // 处理未认证
                .and()
                .csrf().disable()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }



}
