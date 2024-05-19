package org.celltinel.e2e.encryption.config;

import org.celltinel.e2e.encryption.logging.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class LoggingConfiguration {

    @Bean
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }

}