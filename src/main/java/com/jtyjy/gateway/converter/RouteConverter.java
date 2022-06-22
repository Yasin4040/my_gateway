package com.jtyjy.gateway.converter;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jtyjy.gateway.repository.model.GatewayRoute;
import com.jtyjy.gateway.vo.RouteVO;
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
public interface RouteConverter {
    RouteConverter INSTANCE = Mappers.getMapper( RouteConverter.class );
    /**
    * 转换实体
    * @param route 实体 转换DTO
    * @return  RouteVO
    */
    @Mapping(target = "id",source = "id")
    RouteVO toRouteVO(GatewayRoute route);
    /**
     * 转换实体list
     * @param  routes   实体 转换DTO
     * @return  List<RouteVO>
     */
    List<RouteVO> toRouteVOList(List<GatewayRoute> routes);


    Page<RouteVO> toPageRouteVO(Page<GatewayRoute> routePage);

}
