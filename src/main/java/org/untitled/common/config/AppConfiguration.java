package org.untitled.common.config;

import org.untitled.common.config.annotation.EnableErrorHandler;
import org.untitled.common.config.annotation.EnableLogInterceptor;
import org.untitled.common.config.annotation.EnableSwagger;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger
@EnableErrorHandler
@EnableLogInterceptor
public class AppConfiguration {
}