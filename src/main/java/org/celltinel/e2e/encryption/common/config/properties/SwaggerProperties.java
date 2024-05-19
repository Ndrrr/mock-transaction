package org.celltinel.e2e.encryption.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private String title = ApplicationDefaults.Swagger.title;
    private String description = ApplicationDefaults.Swagger.description;
    private String version = ApplicationDefaults.Swagger.version;
    private String termsOfServiceUrl = ApplicationDefaults.Swagger.termsOfServiceUrl;
    private String contactName = ApplicationDefaults.Swagger.contactName;
    private String contactUrl = ApplicationDefaults.Swagger.contactUrl;
    private String contactEmail = ApplicationDefaults.Swagger.contactEmail;
    private String license = ApplicationDefaults.Swagger.license;
    private String licenseUrl = ApplicationDefaults.Swagger.licenseUrl;
    private String defaultIncludePattern = ApplicationDefaults.Swagger.defaultIncludePattern;
    private String host = ApplicationDefaults.Swagger.host;
    private String[] protocols = ApplicationDefaults.Swagger.protocols;
    private boolean useDefaultResponseMessages =
            ApplicationDefaults.Swagger.useDefaultResponseMessages;
    private boolean useAuthorizationHeader =
            ApplicationDefaults.Swagger.useAuthorizationHeader;
    private String basePackage = ApplicationDefaults.Swagger.basePackage;

}