package com.comerlato.voting_challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SpringDocConfig {

    @Value("#{'${springdoc.swagger-ui.server.list}'.split(',')}")
    private List<String> servers;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(servers.stream().map(s -> new Server().url(s)).collect(Collectors.toList()))
                .info(new Info().title("Voting Challenge")
                        .description("Voting Challenge API")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
