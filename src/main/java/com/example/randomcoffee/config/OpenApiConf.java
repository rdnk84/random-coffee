package com.example.randomcoffee.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Веб-приложение по поиску коллеги для улучшения коммуникации в офисе", version = "2.0"))
public class OpenApiConf {
}
