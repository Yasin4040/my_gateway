package com.jtyjy.gateway.timer;

import com.jtyjy.gateway.service.GatewayRouteService;
import com.jtyjy.gateway.service.IpBlackService;
import com.jtyjy.gateway.service.WhiteListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Created by ZiYao Lee on 2022/07/07.
 * Time: 11:02
 */
@Slf4j
@Component
public class TimerRefreshRouteTask {
    @Autowired
    private IpBlackService ipBlackService;
    @Autowired
    private WhiteListService whiteListService;
    @Autowired
    private GatewayRouteService gatewayRouteService;

    /**
     * 每5分钟执行一次缓存同步
     */
    @Scheduled(fixedRate = 5*60*1000)
    public void syncRouteCache(){
        log.info("执行定时任务：同步刷新到路由、白名单、黑IP等配置...");
        gatewayRouteService.reloadConfig();
        whiteListService.refreshPathList();
        ipBlackService.refreshIpListNoEvent();
    }
}
