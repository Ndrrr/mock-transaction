package org.celltinel.e2e.encryption.common.error;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.celltinel.e2e.encryption.common.util.RequestContextUtil;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String uuid;
    private String code;
    private Integer status;
    private String method;
    private String path;
    private String message;
    private LocalDateTime timestamp;
    private List<ValidationError> errors;
    private Map<String, Object> details;

    public static ErrorResponse fromServiceException(ServiceException ex) {
        return ErrorResponse.builder()
                .uuid(ex.getUuid())
                .code(ex.getCodeAsStr())
                .status(ex.getStatus())
                .method(ex.getMethod())
                .path(ex.getPath())
                .message(ex.getMessage())
                .timestamp(ex.getTimestamp())
                .details(ex.getDetails())
                .build();
    }

    public static ErrorResponse build(String uuid,
                                      HttpStatus status,
                                      String message,
                                      String method,
                                      String path,
                                      LocalDateTime timestamp,
                                      List<ValidationError> errors) {
        return ErrorResponse.builder()
                .uuid(uuid)
                .code(status.name())
                .status(status.value())
                .message(message)
                .method(method)
                .path(path)
                .timestamp(timestamp)
                .errors(errors)
                .build();
    }

    public static ErrorResponse build(String uuid,
                                      HttpStatus status,
                                      String message,
                                      String method,
                                      String path,
                                      LocalDateTime timestamp) {
        return build(uuid, status, message, method, path, timestamp, Collections.emptyList());

    }

    public static ErrorResponse build(String uuid,
                                      HttpStatus status,
                                      String message,
                                      List<ValidationError> errors) {
        return build(uuid,
                status,
                message,
                RequestContextUtil.getMethod(),
                RequestContextUtil.getPath(),
                LocalDateTime.now(),
                errors);
    }

    public static ErrorResponse build(String uuid,
                                      HttpStatus status,
                                      String message) {
        return build(uuid, status, message, Collections.emptyList());
    }

}