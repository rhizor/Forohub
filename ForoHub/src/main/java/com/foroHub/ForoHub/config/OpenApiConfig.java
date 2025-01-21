package com.foroHub.ForoHub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API del Foro ForoHub")
                        .description("API REST para gestionar tópicos, usuarios y respuestas en ForoHub")
                        .version("1.0"));
    }
}
