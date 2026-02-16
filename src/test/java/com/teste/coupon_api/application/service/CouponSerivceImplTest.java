package com.teste.coupon_api.application.service;

import com.teste.coupon_api.domain.exception.BusinessException;
import com.teste.coupon_api.domain.model.Coupon;
import com.teste.coupon_api.domain.model.CouponStatus;
import com.teste.coupon_api.infra.adapters.in.dto.CouponRequestDTO;
import com.teste.coupon_api.infra.adapters.in.dto.CouponResponseDTO;
import com.teste.coupon_api.infra.adapters.in.mapper.CouponMapper;
import com.teste.coupon_api.infra.adapters.out.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponSerivceImplTest {

    @InjectMocks
    private CouponSerivceImpl service;
    @Mock
    private CouponRepository repository;
    @Mock
    private CouponMapper mapper;
    @Captor
    private ArgumentCaptor<Coupon> couponCaptor;

    private CouponRequestDTO validRequest;
    private CouponResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        validRequest = CouponRequestDTO.builder()
                .code("A!B@C#1-D2")
                .description("desc")
                .discountValue(new BigDecimal("0.5"))
                .published(true)
                .expirationDate(LocalDateTime.of(2030, 12, 20, 10, 30))
                .build();

        responseDTO = CouponResponseDTO.builder()
                .id(1L)
                .code("ABC1D2")
                .description("desc")
                .discountValue(new BigDecimal("0.5"))
                .published(true)
                .expirationDate(LocalDateTime.of(2030, 12, 20, 10, 30))
                .status(CouponStatus.ACTIVE)
                .build();
    }

    @DisplayName("create deve salvar e rertornar um response com sucesso")
    @Test
    void create_shouldSaveAndReturnResponse() {
        Coupon savedEntity = new Coupon(validRequest);
        when(repository.save(any(Coupon.class))).thenReturn(savedEntity);
        when(mapper.toResponseDTO(savedEntity)).thenReturn(responseDTO);

        CouponResponseDTO result = service.create(validRequest);

        assertNotNull(result);
        assertEquals(responseDTO.id(), result.id());
        assertEquals(responseDTO.code(), result.code());
        assertEquals(responseDTO.status(), result.status());

        verify(repository, times(1)).save(couponCaptor.capture());
        Coupon captured = couponCaptor.getValue();
        assertNotNull(captured);
        assertEquals(validRequest.description(), captured.getDescription());
        assertEquals(0, validRequest.discountValue().compareTo(captured.getDiscountValue()));
        assertEquals(validRequest.expirationDate(), captured.getExpirationDate());
        verify(mapper, times(1)).toResponseDTO(savedEntity);
    }

    @DisplayName("delete deve alterar status para DELETED e salvar")
    @Test
    void delete_shouldSoftDeleteAndSave() {
        var deletedResponse = CouponResponseDTO.builder().status(CouponStatus.DELETED).build();
        Coupon existing = new Coupon(validRequest);
        assertNotEquals(CouponStatus.DELETED, existing.getStatus());

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Coupon.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponseDTO(any(Coupon.class))).thenReturn(deletedResponse);

        service.delete(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(couponCaptor.capture());
        Coupon saved = couponCaptor.getValue();
        assertEquals(CouponStatus.DELETED, saved.getStatus());
        assertNotNull(saved.getDeletedAt());

        verify(mapper, times(1)).toResponseDTO(saved);
    }

    @DisplayName("delete deve lançar exceção quando não encotrar o cupom")
    @Test
    void delete_whenNotFound_shouldThrowBusinessException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> service.delete(1L));
        assertEquals("Cupom não encontrado", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), ex.getCode());

        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any());
        verify(mapper, never()).toResponseDTO(any());
    }

    @DisplayName("delete deve lançar exceção quando status já estiver marcado como DELETED")
    @Test
    void delete_whenAlreadyDeleted_shouldThrowBusinessExceptionAndNotSave() {
        Coupon existing = Coupon.builder()
                .id(1L)
                .status(CouponStatus.DELETED)
                .deletedAt(LocalDateTime.now())
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        BusinessException ex = assertThrows(BusinessException.class, () -> service.delete(1L));
        assertEquals("Este cupom já foi deletado", ex.getMessage());

        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any());
        verify(mapper, never()).toResponseDTO(any());
    }
}