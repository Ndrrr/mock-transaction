package org.untitled.common.config.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.untitled.common.config.properties.LoggingProperties;
import org.untitled.common.config.LoggingInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Target(TYPE)
@Retention(RUNTIME)
@Import(LoggingInterceptor.class)
@EnableConfigurationProperties(LoggingProperties.class)
public @interface EnableLogInterceptor {
}