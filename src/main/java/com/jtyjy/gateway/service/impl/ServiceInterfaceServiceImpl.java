package com.jtyjy.gateway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jtyjy.gateway.converter.ServiceInterfaceConverter;
import com.jtyjy.gateway.query.ServiceInterfaceQuery;
import com.jtyjy.gateway.repository.model.ServiceInterface;
import com.jtyjy.gateway.service.ServiceInterfaceService;
import com.jtyjy.gateway.repository.mapper.ServiceInterfaceMapper;
import com.jtyjy.gateway.vo.ServiceInterfaceVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author User
* @description 针对表【gw_service_interface】的数据库操作Service实现
* @createDate 2022-07-02 17:34:04
*/
@Service
public class ServiceInterfaceServiceImpl extends ServiceImpl<ServiceInterfaceMapper, ServiceInterface>
    implements ServiceInterfaceService{

    @Override
    public Page<ServiceInterfaceVO> selectServiceInterfacePageVo(ServiceInterfaceQuery query) {
        Page<ServiceInterface> page = this.page(new Page<>(query.getPageNum(), query.getPageSize()), new LambdaQueryWrapper<ServiceInterface>()
                .like(StringUtils.isNotBlank(query.getServiceId()), ServiceInterface::getServiceId, query.getServiceId())
                .like(StringUtils.isNotBlank(query.getPath()), ServiceInterface::getPath, query.getPath())
                .like(StringUtils.isNotBlank(query.getTag()), ServiceInterface::getTag, query.getTag())
                .like(StringUtils.isNotBlank(query.getSummary()), ServiceInterface::getSummary, query.getSummary())
                .like(StringUtils.isNotBlank(query.getServiceUrl()), ServiceInterface::getServiceUrl, query.getServiceUrl())
        );
        return ServiceInterfaceConverter.INSTANCE.toPageServiceInterfaceVO(page);
    }
}




