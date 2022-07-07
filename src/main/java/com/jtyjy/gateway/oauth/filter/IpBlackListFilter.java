package com.jtyjy.gateway.oauth.filter;

import com.jtyjy.gateway.cache.IpListCache;
import com.jtyjy.gateway.constants.StringConstants;
import com.jtyjy.gateway.service.IpBlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ipresolver.RemoteAddressResolver;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

import static com.jtyjy.gateway.constants.CacheNameConstants.BLACKLIST_IP_KEY;

/**
 * Description:
 * Created by ZiYao Lee on 2022/07/04.
 * Time: 14:34
 */
@Slf4j
@Component
public class IpBlackListFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private IpBlackService ipBlackService;

    private final RemoteAddressResolver remoteAddressResolver = XForwardedRemoteAddressResolver
            .maxTrustedIndex(1);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            InetSocketAddress remoteAddress = remoteAddressResolver.resolve(exchange);
//            String clientIp = remoteAddress.getHostName();
            String clientIp = remoteAddress.getAddress().getHostAddress();
            if (IpListCache.get(clientIp)!=null) {
                log.info("intercept invalid request from forbidden ip {}", clientIp);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return Mono.empty();
            }
        } catch (Exception e) {
            log.error("IpBlackListFilter error", e);
        }
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        //先于认证。 最开始进入
        return -1;
    }
}
