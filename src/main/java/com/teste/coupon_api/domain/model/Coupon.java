package com.teste.coupon_api.domain.model;

import com.teste.coupon_api.domain.exception.BusinessException;
import com.teste.coupon_api.infra.adapters.in.dto.CouponRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 6, unique = true)
    private String code;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private boolean published;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Coupon(CouponRequestDTO requestDTO) {
        this.code = sanitizeCode(requestDTO.code());
        this.description = requestDTO.description();
        this.discountValue = requestDTO.discountValue();
        this.expirationDate = requestDTO.expirationDate();
        this.published = requestDTO.published();
        this.status = CouponStatus.ACTIVE;
    }

    public void softDelete() {
        if(this.status.equals(CouponStatus.DELETED)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "Este cupom já foi deletado");
        }
        this.status = CouponStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }

    private String sanitizeCode(String raw) {
        String clean = raw.replaceAll("[^a-zA-Z0-9]", "");
        return clean.substring(0, 6);
    }
}
