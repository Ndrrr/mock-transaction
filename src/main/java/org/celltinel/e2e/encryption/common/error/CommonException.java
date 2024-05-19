package org.celltinel.e2e.encryption.common.error;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException {

    private String uuid;
    private Integer status;
    private String method;
    private String path;
    private String message;
    private LocalDateTime timestamp;

    public CommonException(String uuid,
                           HttpStatus status,
                           String path,
                           String method,
                           String message,
                           LocalDateTime timestamp) {
        Objects.requireNonNull(status, "HttpStatus must be defined!");
        this.uuid = uuid;
        this.status = status.value();
        this.path = path;
        this.method = method;
        this.message = message;
        this.timestamp = timestamp;
    }

}