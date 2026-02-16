package com.teste.coupon_api.infra.adapters.in.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CouponRequestDTO(
        @Schema(description = "Código do cupom. Caracteres especiais serão removidos e o sistema garantirá 6 caracteres.",
                example = "ABC123", requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(min = 6, message = "Informe no mínimo 6 dígitos")
        String code,

        @Schema(description = "Descrição detalhada do cupom", example = "Cupom de 20% para a Black Friday",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Campo obrigatório")
        String description,

        @Schema(description = "Valor do desconto. Deve ser no mínimo 0.5.", example = "25.50", minimum = "0.5",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Campo obrigatório")
        @DecimalMin(value = "0.5", message = "O valor de discountValue deve ser de pelo menos 0,5.")
        BigDecimal discountValue,

        @Schema(description = "Define se o cupom já nasce publicado e ativo para uso", example = "true")
        boolean published,

        @Schema(description = "Data de expiração do cupom. Não pode ser no passado.",
                example = "2025-12-31 23:59", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Campo obrigatório")
        @FutureOrPresent(message = "expirationDate não pode ser no passado")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime expirationDate
)
{}
