package com.jtyjy.gateway.converter;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtyjy.gateway.dto.InterfaceDTO;
import com.jtyjy.gateway.repository.model.ServiceInterface;
import com.jtyjy.gateway.vo.ServiceInterfaceVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Description:
 *
 * @author ZiYao Lee
 * @date 2022/06/20
 * Time: 17:46
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceInterfaceConverter {
    ServiceInterfaceConverter INSTANCE = Mappers.getMapper( ServiceInterfaceConverter.class );
    /**
    * 转换实体
    * @param serviceInterface 实体 转换DTO
    * @return  ServiceInterfaceVO
    */
    @Mapping(target = "id",source = "id")
    ServiceInterfaceVO toServiceInterfaceVO(ServiceInterface serviceInterface);
    /**
     * 转换实体list
     * @param  routes   实体 转换DTO
     * @return  List<RouteVO>
     */
    List<ServiceInterfaceVO> toServiceInterfaceVOList(List<ServiceInterface> routes);


    Page<ServiceInterfaceVO> toPageServiceInterfaceVO(Page<ServiceInterface> routePage);


    /***
    * 转换实体
    * @param dto	 实体 转换DTO
    * @return com.jtyjy.gateway.repository.model.ServiceInterface
    * @author ZiYao Lee
    * @date 2022/7/2
    */
    ServiceInterface toServiceInterface(InterfaceDTO dto);

    List<ServiceInterface> toServiceInterfaceList(List<InterfaceDTO> dtoList);

}
