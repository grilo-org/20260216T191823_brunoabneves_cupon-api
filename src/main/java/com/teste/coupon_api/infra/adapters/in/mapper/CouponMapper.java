package com.teste.coupon_api.infra.adapters.in.mapper;

import com.teste.coupon_api.domain.model.Coupon;
import com.teste.coupon_api.infra.adapters.in.dto.CouponResponseDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CouponMapper {

    CouponResponseDTO toResponseDTO(Coupon entity);
}
