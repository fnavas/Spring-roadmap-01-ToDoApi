package com.fnavas.ToDoApi.configuration;

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
                .title("ToDo API")
                .version("1.0")
                .description("API for managing ToDo items")
                .contact(new Contact()
                    .name("Fernando Navas")
                    .email("fnavas.dev@gmail.com")
                    .url("https://www.github.com/fnavas")));


    }
}
