package com.jtyjy.gateway.service.impl;

import com.jtyjy.basic.common.web.Result;
import com.jtyjy.gateway.cache.IpListCache;
import com.jtyjy.gateway.constants.StringConstants;
import com.jtyjy.gateway.dto.PageBody;
import com.jtyjy.gateway.event.DataIpApplicationEvent;
import com.jtyjy.gateway.service.IpBlackService;
import com.jtyjy.gateway.vo.IpBlackVO;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.jtyjy.gateway.constants.CacheNameConstants.BLACKLIST_IP_KEY;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxw
 * @since 2022-03-15
 */
@Service
public class IpBlackServiceImpl implements IpBlackService, ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private StringRedisTemplate redisTemplate;
    public static FastDateFormat ymd = FastDateFormat.getInstance("yyyy-MM-dd");

    @Override
    public Mono<Result> getIpList() {
        return Mono.just( Result.ok(new PageBody(getIpBlackList())));
    }

    @Override
    public void refreshIpList() {
        refreshIpListNoEvent();
        this.applicationEventPublisher.publishEvent(new DataIpApplicationEvent(this));
    }

    @Override
    public void refreshIpListNoEvent() {
        IpListCache.clear();
        redisTemplate.keys(BLACKLIST_IP_KEY + StringConstants.COLON + StringConstants.ASTERISK).stream().
                collect(Collectors.toMap(key -> key.substring(key.indexOf(StringConstants.COLON)+1, key.length()),
                        key -> redisTemplate.opsForValue().get(key)))
                .forEach((k,v)->{
                    //存入k,b值
                    IpListCache.put(k,v);
                });
    }

    @Override
    public Mono<Result> addIp(String ip, String remark) {
        String format = ymd.format(System.currentTimeMillis());
        redisTemplate.opsForValue().set(BLACKLIST_IP_KEY + StringConstants.COLON + ip,   format+ StringConstants.COMMA + remark, 1, TimeUnit.DAYS);
        //存入ip
        IpListCache.put(ip,remark);
        return Mono.just(Result.ok());
    }
    @Override
    public Mono<Result> delIp(String ip) {
        redisTemplate.delete(BLACKLIST_IP_KEY + StringConstants.COLON + ip);
        //移除ip
        IpListCache.remove(ip);
        return Mono.just(Result.ok());
    }
    @Override
    public List<IpBlackVO>  getIpBlackList(){
       return IpListCache.getAll();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
