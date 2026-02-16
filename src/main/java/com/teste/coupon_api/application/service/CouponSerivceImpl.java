package com.teste.coupon_api.application.service;

import com.teste.coupon_api.domain.exception.BusinessException;
import com.teste.coupon_api.domain.model.Coupon;
import com.teste.coupon_api.domain.ports.CouponSerivce;
import com.teste.coupon_api.infra.adapters.in.dto.CouponRequestDTO;
import com.teste.coupon_api.infra.adapters.in.dto.CouponResponseDTO;
import com.teste.coupon_api.infra.adapters.in.mapper.CouponMapper;
import com.teste.coupon_api.infra.adapters.out.CouponRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponSerivceImpl implements CouponSerivce {

    private final CouponRepository repository;
    private final CouponMapper mapper;

    @Transactional
    @Override
    public CouponResponseDTO create(CouponRequestDTO couponRequestDTO) {
        var coupon = new Coupon(couponRequestDTO);
        CouponResponseDTO responseDTO = mapper.toResponseDTO(repository.save(coupon));
        log.info("Cupom criado com sucesso. Cupom: {}", responseDTO);
        return responseDTO;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Coupon coupon = repository.findById(id).orElseThrow(() -> {
            log.info("Cupom não encontrado. id: {}", id);
            return new BusinessException(HttpStatus.NOT_FOUND.value(), "Cupom não encontrado");
        });
        coupon.softDelete();
        CouponResponseDTO responseDTO = mapper.toResponseDTO(repository.save(coupon));
        log.info("Cupom deletado com sucesso. Cupom: {}", responseDTO);
    }
}
