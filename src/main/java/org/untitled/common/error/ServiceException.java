package org.untitled.common.error;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import org.untitled.common.util.RequestContextUtil;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends CommonException {

    private String code;
    private Map<String, Object> details = Collections.emptyMap();

    public ServiceException(String code, HttpStatus status, String message) {
        super(UUID.randomUUID().toString(),
                status,
                RequestContextUtil.getPath(),
                RequestContextUtil.getMethod(),
                message,
                LocalDateTime.now());
        this.code = code;
    }

    public ServiceException(String code, String message) {
        this(code, HttpStatus.BAD_REQUEST, message);
    }

    public String getCodeAsStr() {
        return code == null ? null : code.toLowerCase();
    }

    public String formatProperties() {
        return Optional.ofNullable(details)
                .map(Map::keySet)
                .orElse(Collections.emptySet())
                .stream()
                .map(this::formatProperty)
                .collect(Collectors.joining(", "));
    }

    private String formatProperty(String key) {
        return key + ": " + details.get(key);
    }

}