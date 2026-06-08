package com.travelsphere.admin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TravelSphere Admin Service API")
                        .description("Inventory management, analytics, fraud alerts, and support APIs")
                        .version("1.0.0")
                        .contact(new Contact().name("TravelSphere Team").email("dev@travelsphere.com"))
                        .license(new License().name("Proprietary")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer"))
                .schemaRequirement("Bearer", new SecurityScheme()
                        .name("Bearer").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
    }
}
