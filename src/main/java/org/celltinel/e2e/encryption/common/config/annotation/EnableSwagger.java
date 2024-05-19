package org.celltinel.e2e.encryption.common.config.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.celltinel.e2e.encryption.common.config.SwaggerConfiguration;
import org.celltinel.e2e.encryption.common.config.properties.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Target(TYPE)
@Retention(RUNTIME)
@EnableConfigurationProperties(SwaggerProperties.class)
@Import(SwaggerConfiguration.class)
public @interface EnableSwagger {
}
