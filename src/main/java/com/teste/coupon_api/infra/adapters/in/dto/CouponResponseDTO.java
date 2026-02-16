package com.teste.coupon_api.infra.adapters.in.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.teste.coupon_api.domain.model.CouponStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CouponResponseDTO(
        Long id,
        String code,
        String description,
        BigDecimal discountValue,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime expirationDate,
        boolean published,
        CouponStatus status,
        LocalDateTime createdAt,
        LocalDateTime deletedAt
) {}
