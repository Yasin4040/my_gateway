package com.jtyjy.gateway.oauth;

import com.jtyjy.gateway.service.WhiteListService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 自定义鉴权管理器
 * 用于判断是否有资源的访问权限
 * 可以处理白名单
 * 权限控制
 */
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final WhiteListService whiteListService;
    private final String env;

    public AuthorizationManager(ApplicationContext applicationContext){
        whiteListService = applicationContext.getBean(WhiteListService.class);
        Environment environment = applicationContext.getEnvironment();
        env = environment.getProperty("spring.profiles.active");
    }

//    @Override
//    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
//        whiteListService.getPathList()
//    /*// 1、从Redis中获取当前路径可访问角色列表
//    URI uri = authorizationContext.getExchange().getRequest().getURI();
//    Object obj = redisTemplate.opsForHash().get(RedisConstant.RESOURCE_ROLES_MAP, uri.getPath());
//    List<String> authorities = Convert.toList(String.class, obj);
//    authorities = authorities.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());
//    // 2、认证通过且角色匹配的用户可访问当前路径
//    return mono
//        .filter(Authentication::isAuthenticated)
//        .flatMapIterable(Authentication::getAuthorities)
//        .map(GrantedAuthority::getAuthority)
//        .any(authorities::contains)
//        .map(AuthorizationDecision::new)
//        .defaultIfEmpty(new AuthorizationDecision(false));*/
//        /*return mono.map(authentication -> {
//            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
//            JSONArray jsonArray = (JSONArray) jwtAuthenticationToken.getTokenAttributes().get("scope");
//            //authorizationContext.getExchange().getRequest().getPath()
//            //jsonArray.contains()
//            return getAuthorizationDecision(authentication);
//        }).defaultIfEmpty(new AuthorizationDecision(false));*/
//
//        return Mono.just(new AuthorizationDecision(true));
//    }


    private AuthenticationTrustResolver authTrustResolver = new AuthenticationTrustResolverImpl();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        String path = authorizationContext.getExchange().getRequest().getPath().value();
        for (String p : whiteListService.getPathList()){
            if(antPathMatcher.match(p, path)){
                return Mono.just(new AuthorizationDecision(true));
            }
        }
        return authentication.filter(this::isNotAnonymous)
                .filter(this::hasResourceId)
                .map(this::getAuthorizationDecision)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private AuthorizationDecision getAuthorizationDecision(Authentication authentication) {
        return new AuthorizationDecision(authentication.isAuthenticated());
    }

    /**
     * Verify (via {@link AuthenticationTrustResolver}) that the given authentication is
     * not anonymous.
     * @param authentication to be checked
     * @return <code>true</code> if not anonymous, otherwise <code>false</code>.
     */
    private boolean isNotAnonymous(Authentication authentication) {
        return !this.authTrustResolver.isAnonymous(authentication);
    }

    private boolean hasResourceId(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            List<String> aud = (List<String>) jwtAuthenticationToken.getTokenAttributes().get("aud");
            if (CollectionUtils.isNotEmpty(aud)) {
                return aud.contains(env);
            }
        }
        return false;
    }

}
