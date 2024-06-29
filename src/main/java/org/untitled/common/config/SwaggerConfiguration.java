package org.untitled.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.untitled.common.config.properties.SwaggerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String API_KEY_NAME = "JWT";
    private static final String AUTHORIZATION_SCHEME = "Bearer";

    private final SwaggerProperties properties;

    public SwaggerConfiguration(SwaggerProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .schemaRequirement(AUTHORIZATION_SCHEME, securityScheme())
                .addSecurityItem(securityRequirement());
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList(AUTHORIZATION_SCHEME);
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(AUTHORIZATION_HEADER)
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat(API_KEY_NAME);
    }

    private Info apiInfo() {
        return new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .termsOfService(properties.getTermsOfServiceUrl())
                .contact(apiContact())
                .license(apiLicence());
    }

    private License apiLicence() {
        return new License()
                .name(properties.getLicense())
                .url(properties.getLicenseUrl());
    }

    private Contact apiContact() {
        return new Contact()
                .name(properties.getContactName())
                .email(properties.getContactEmail())
                .url(properties.getContactUrl());
    }

}