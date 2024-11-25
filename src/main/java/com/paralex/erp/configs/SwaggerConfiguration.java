package com.paralex.erp.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Paralex Software Backend")
                        .description("Documentation for Paralex Software")
                        .version("1.0.0")
                        .contact(new Contact().name("Israel Oluwole").email("ioluwole@evryword.com.ng"))
                        .license(new License().name("© 2024 Paralex")
                                .url("https://parallexapp.com/")))
                .addSecurityItem(new SecurityRequirement().addList("Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Header Token", createAuthorizationScheme())
                        .addSecuritySchemes("Cookie Token", createSessionScheme()));
    }

    private SecurityScheme createSessionScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("session");
    }

    private SecurityScheme createAuthorizationScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT");
    }
}
