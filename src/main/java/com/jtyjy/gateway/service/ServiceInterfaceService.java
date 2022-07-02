package com.jtyjy.gateway.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtyjy.gateway.query.ServiceInterfaceQuery;
import com.jtyjy.gateway.repository.model.ServiceInterface;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jtyjy.gateway.vo.RouteVO;
import com.jtyjy.gateway.vo.ServiceInterfaceVO;

/**
* @author User
* @description 针对表【gw_service_interface】的数据库操作Service
* @createDate 2022-07-02 17:34:04
*/
public interface ServiceInterfaceService extends IService<ServiceInterface> {

    Page<ServiceInterfaceVO> selectServiceInterfacePageVo(ServiceInterfaceQuery query);
}
