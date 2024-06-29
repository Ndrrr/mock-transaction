package org.untitled.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("security")
public class SecurityProperties {

    private String privateKey;
    private String publicKey;

}
