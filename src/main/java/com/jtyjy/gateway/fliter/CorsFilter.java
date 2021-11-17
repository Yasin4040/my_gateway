package com.jtyjy.gateway.fliter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 处理多个跨域头问题，只返回一个，否则浏览器会报异常
 */
@Component
public class CorsFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        // 指定此过滤器位于NettyWriteResponseFilter之后
        // 即待处理完响应体后接着处理响应头
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER + 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.defer(() -> {
            HttpHeaders headers = exchange.getResponse().getHeaders();
            headers.computeIfAbsent(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, key -> Stream.of("*").collect(Collectors.toList()));
            headers.computeIfAbsent(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, key -> Stream.of("*").collect(Collectors.toList()));
            headers.computeIfAbsent(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, key -> Stream.of("*").collect(Collectors.toList()));
            List<String> vary = headers.computeIfAbsent(HttpHeaders.VARY, key -> Stream.of("Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers").collect(Collectors.toList()));
            if(!vary.contains("Origin")){
                vary.add("Origin");
                vary.add("Access-Control-Request-Method");
                vary.add("Access-Control-Request-Headers");
            }
            /*exchange.getResponse().getHeaders().entrySet().stream()
                    .filter(kv -> (kv.getValue() != null && kv.getValue().size() > 1))
                    .filter(kv -> (kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                            || kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)))
                    .forEach(kv -> {
                        List<String> list = new ArrayList<>();
                        list.add(kv.getValue().get(0));
                        kv.setValue(list);
                    });*/

            return chain.filter(exchange);
        }));
    }
}
