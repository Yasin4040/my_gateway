package com.jtyjy.gateway.cache;

import com.google.common.collect.Lists;
import com.jtyjy.gateway.vo.IpBlackVO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.sf.jsqlparser.parser.feature.Feature.values;

/**
 * Description:
 * Created by ZiYao Lee on 2022/07/07.
 * Time: 10:52
 */
public class IpListCache {
    private static ConcurrentHashMap<String,Object> cacheMap = new ConcurrentHashMap<>();

    public static void put(final String key,final Object value){
        Assert.notNull(key, "hash map key cannot is null");
        Assert.notNull(value, "hash map value cannot is null");
        cacheMap.put(key, value);
    }

    public static Object get(final String key){
        return cacheMap.get(key);
    }

    public static synchronized void remove(final String key){
        if (cacheMap.containsKey(key)){
            cacheMap.remove(key);
        }
    }
    public static List<IpBlackVO> getAll(){
        List<IpBlackVO> ipList = new ArrayList<>();
        for (Map.Entry<String, Object> entity : cacheMap.entrySet()) {
            IpBlackVO vo = new IpBlackVO();
            vo.setIp(entity.getKey());
            vo.setRemark((String) entity.getValue());
            ipList.add(vo);
        }
        return ipList;
    }
    public static synchronized void clear(){
        cacheMap.clear();
    }
}
