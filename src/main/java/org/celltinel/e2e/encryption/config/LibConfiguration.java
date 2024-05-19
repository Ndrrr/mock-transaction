package org.celltinel.e2e.encryption.config;

import org.celltinel.e2e.encryption.common.config.annotation.EnableErrorHandler;
import org.celltinel.e2e.encryption.common.config.annotation.EnableLogInterceptor;
import org.celltinel.e2e.encryption.common.config.annotation.EnableSwagger;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger
@EnableErrorHandler
@EnableLogInterceptor
public class LibConfiguration {
}