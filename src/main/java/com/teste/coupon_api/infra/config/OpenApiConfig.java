package com.teste.coupon_api.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Cupons")
                        .version("1.0.0")
                        .description("Sistema para cadastro e controle de cupons")
                        .contact(new Contact()
                                .name("Bruno Neves")
                                .email("brunoabneves97@gmail.com")
                                .url("https://github.com/brunoabneves")));
    }
}