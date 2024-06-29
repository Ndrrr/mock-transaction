package org.untitled.common.config.properties;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = "logging")
public class LoggingProperties {

    private List<String> ignorePaths;
    private Boolean enableLogInterceptor;

    public Boolean getEnableLogInterceptor() {
        return Objects.nonNull(enableLogInterceptor) ? enableLogInterceptor : Boolean.FALSE;
    }

    public List<String> getIgnorePaths() {
        return Objects.nonNull(ignorePaths) ? ignorePaths : Collections.emptyList();
    }

}
