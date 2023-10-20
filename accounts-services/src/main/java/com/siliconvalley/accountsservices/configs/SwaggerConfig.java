package com.siliconvalley.accountsservices.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(getApiInfo());
        docket.securityContexts(Collections.singletonList(getSecurityContext()));
        docket.securitySchemes(List.of(getSchemes()));
        ApiSelectorBuilder select = docket.select();
        select.apis(RequestHandlerSelectors.any());
        select.paths(PathSelectors.any());
        return select.build();
    }


    private SecurityContext getSecurityContext() {

        SecurityContext context = SecurityContext
                .builder()
                .securityReferences(getSecurityReferences())
                .build();
        return context;
    }

    private List<SecurityReference> getSecurityReferences() {
        AuthorizationScope[] scopes = {new AuthorizationScope("Global", "Access Every Thing")};
        return List.of(new SecurityReference("JWT", scopes));

    }

    private ApiKey getSchemes() {
        return new ApiKey("JWT", "Authorization", "header");
    }


    private ApiInfo getApiInfo() {

        return new ApiInfo(
                "Silicion Valley Accounts Service Backend : APIS ",
                "Accounts Service that deals with account creation,deletion,re opening,all kinds of transactions to accnt and benefficiary",
                "1.0.0V",
                "Protected under SiliconValley Copyright policy",
                new Contact("phoenix", "https://www.linkedin.com/in/bhaskar-kumar-das-64019a168/", "bhakarkumardas9@gmail.com"),
                "License of APIS",
                "https://www.linkedin.com/in/bhaskar-kumar-das-64019a168/",
                new ArrayDeque<>()
        );
    }
}
