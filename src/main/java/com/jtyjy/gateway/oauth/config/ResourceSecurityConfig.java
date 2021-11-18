package com.jtyjy.gateway.oauth.config;

import com.jtyjy.gateway.oauth.AuthorizationManager;
import com.jtyjy.gateway.oauth.handler.RestAuthenticationEntryPoint;
import com.jtyjy.gateway.oauth.handler.RestfulAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.server.ServerWebExchange;

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
    @Autowired
    private CorsWebFilter corsWebFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.authorizeExchange()
                // 允许actuator endpoints不进行认证
                .matchers(EndpointRequest.toAnyEndpoint()).permitAll()
                // SCOPE_ 前缀对应认证服务器的客户端 scopes(...) 配置
                //.pathMatchers("/api").hasAuthority("SCOPE_api")
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/v2/**", "/v3/**", "/swagger-resources/**",
                        "/doc.html", "/webjars/**", "/auth/user/**", "/auth/emp/**").permitAll() //白名单
                //.pathMatchers("/*.js").authenticated()
                //.anyExchange().permitAll()
                .anyExchange().authenticated() //默认的鉴权管理
                //.anyExchange().access(new AuthorizationManager()) // 自定义鉴权管理器配置
                .and().exceptionHandling()
                // 处理未授权
                .accessDeniedHandler(restfulAccessDeniedHandler)
                // 处理未认证
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                //添加跨域拦截器
                .addFilterAfter(corsWebFilter, SecurityWebFiltersOrder.CORS)
                .csrf().disable()
                //为资源服务器添加异常处理
                .oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint).accessDeniedHandler(restfulAccessDeniedHandler)
                .jwt();

        return http.build();
    }



}
