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
        if(pathList == null) {
            synchronized (this) {
                if(pathList == null) {
                    pathList = list().stream().map(WhiteList::getPath).collect(Collectors.toList());
                }
            }
        }
        return pathList;
    }

    @Override
    public void refreshPathList() {
        pathList = list().stream().map(WhiteList::getPath).collect(Collectors.toList());
    }
}
