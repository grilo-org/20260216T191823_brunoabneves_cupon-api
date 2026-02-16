package com.teste.coupon_api.domain.ports;

import com.teste.coupon_api.infra.adapters.in.dto.CouponRequestDTO;
import com.teste.coupon_api.infra.adapters.in.dto.CouponResponseDTO;

public interface CouponSerivce {

    CouponResponseDTO create(CouponRequestDTO couponRequestDTO);
    void delete(Long id);
}
