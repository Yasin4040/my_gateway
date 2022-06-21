package com.jtyjy.gateway.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtyjy.gateway.query.WhiteListQuery;
import com.jtyjy.gateway.repository.model.WhiteList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxw
 * @since 2022-03-15
 */
public interface WhiteListService extends IService<WhiteList> {

    List<String> getPathList();

    void refreshPathList();

    Page<WhiteList> selectWhiteListPageVo(WhiteListQuery query);
}
