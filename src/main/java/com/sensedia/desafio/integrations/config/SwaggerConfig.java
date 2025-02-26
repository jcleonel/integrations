package com.sensedia.desafio.integrations.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RESTFul API")
                        .version("v1")
                        .description("Some Descrition")
                        .termsOfService("https://www.google.com/")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.google.com/")
                        )
                );
    }

}