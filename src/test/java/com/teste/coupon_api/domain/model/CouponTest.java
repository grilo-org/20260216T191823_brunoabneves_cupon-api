package com.teste.coupon_api.domain.model;

import com.teste.coupon_api.domain.exception.BusinessException;
import com.teste.coupon_api.infra.adapters.in.dto.CouponRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    private CouponRequestDTO request;

    @BeforeEach
    void setUp() {
        request = CouponRequestDTO.builder()
                .code("A!B@C#1-D2")
                .description("Promo")
                .discountValue(new BigDecimal("1.0"))
                .expirationDate(LocalDateTime.of(2030, 12, 20, 10, 30))
                .published(true).build();
    }

    @Test
    @DisplayName("Deve cadastrar um cupom com sucesso normalizando o código")
    void create_withValidRawCode_normalizesAndCreates() {
        String description = "Promo";
        BigDecimal discount = new BigDecimal("1.0");
        LocalDateTime expiration = LocalDateTime.of(2030, 12, 20, 10, 30);

        Coupon coupon = new Coupon(request);

        assertNotNull(coupon);
        assertEquals("ABC1D2", coupon.getCode());
        assertEquals(description, coupon.getDescription());
        assertEquals(0, discount.compareTo(coupon.getDiscountValue()));
        assertEquals(expiration, coupon.getExpirationDate());
        assertTrue(coupon.isPublished());
        assertEquals(CouponStatus.ACTIVE, coupon.getStatus());
    }

    @Test
    @DisplayName("Soft delete deve alterar status para DELETED e setar data deletedAt. " +
            "Deve lançar exceção quando tentar deletar um registro já marcado como DELETED")
    void softDelete_marksDeletedAndSetsDeletedAt_andCannotDeleteTwice() {
        Coupon c = new Coupon(request);
        assertNotEquals(CouponStatus.DELETED, c.getStatus());

        c.softDelete();

        assertEquals(CouponStatus.DELETED, c.getStatus());
        assertNotNull(c.getDeletedAt());

        BusinessException ex = assertThrows(BusinessException.class, c::softDelete);
        assertEquals("Este cupom já foi deletado", ex.getMessage());
    }
}