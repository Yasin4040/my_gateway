package com.jtyjy.gateway.service.impl;

import com.jtyjy.gateway.repository.model.WhiteList;
import com.jtyjy.gateway.repository.mapper.WhiteListMapper;
import com.jtyjy.gateway.service.WhiteListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zxw
 * @since 2022-03-15
 */
@Service
public class WhiteListServiceImpl extends ServiceImpl<WhiteListMapper, WhiteList> implements WhiteListService {

    private List<String> pathList = null;

    @Override
    public List<String> getPathList() {
        List<String> localPathList = pathList;
        if(localPathList == null) {
            synchronized (this) {
                localPathList = pathList;
                if(localPathList == null) {
                    pathList = localPathList = list().stream().map(WhiteList::getPath).collect(Collectors.toList());
                }
            }
        }
        return localPathList;
    }

    @Override
    public void refreshPathList() {
        pathList = list().stream().map(WhiteList::getPath).collect(Collectors.toList());
    }
}
