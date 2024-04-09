package br.janioofi.msgym.config.infra;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("bearer-key",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("GYMSYS - API")
                        .description("API Rest do aplicação GYMSYS")
                        .contact(new Contact()
                                .name("@janioofi")
                                .email("janioofi@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://gymsys.com.br/api/licenca")));
    }
}
