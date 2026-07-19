package com.demo.siteapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Customises the generated OpenAPI documentation.
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Site Assessment API")
                        .version("1.0")
                        .description("RESTful API for managing prospect site assessments. Secured with HTTP Basic."));
    }
}