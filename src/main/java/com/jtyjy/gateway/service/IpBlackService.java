package com.jtyjy.gateway.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jtyjy.basic.common.web.Result;
import com.jtyjy.gateway.query.WhiteListQuery;
import com.jtyjy.gateway.repository.model.WhiteList;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxw
 * @since 2022-03-15
 */
public interface IpBlackService {

    Mono<Result> getIpList();

    Mono<Result> addIp(String ip, String remark);

    Mono<Result> delIp(String ip);
}
