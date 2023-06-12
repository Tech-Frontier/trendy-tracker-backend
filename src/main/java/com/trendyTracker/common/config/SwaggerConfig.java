package com.trendyTracker.common.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
        /**
         * @return
         */
        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info().title("Trendy-Tracker")
                                                .description("API documentation using springdoc-openapi and OpenAPI 3.0")
                                                .version("1.0.0")
                                                .termsOfService("https://example.com/terms-of-service")
                                                .contact(new Contact().name("Jinsu Jang")
                                                                .email("wlstncjs1234@naver.com")))
                                .components(new Components()
                                                .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")))
                                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt",
                                                Collections.emptyList()))
                                .externalDocs(new ExternalDocumentation()
                                                .description("Documentation for b-side API")
                                                .url("https://example.com/api-docs"));

        }

        @Bean
        public GroupedOpenApi adminApi() {
                return GroupedOpenApi.builder()
                                .group("admin")
                                .packagesToScan("com.trendyTracker.api")
                                .addOperationCustomizer(new OperationCustomizer() {
                                        @Override
                                        public io.swagger.v3.oas.models.Operation customize(
                                                        io.swagger.v3.oas.models.Operation operation,
                                                        HandlerMethod handlerMethod) {
                                                // Add security requirements to admin endpoints
                                                List<SecurityRequirement> securityRequirements = new ArrayList<>();
                                                securityRequirements.add(new SecurityRequirement().addList("bearer-jwt",
                                                                Collections.emptyList()));
                                                operation.setSecurity(securityRequirements);
                                                return operation;
                                        }
                                })
                                .build();
        }
}
