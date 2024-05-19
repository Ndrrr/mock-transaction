package org.celltinel.e2e.encryption.common.error;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.celltinel.e2e.encryption.common.util.ExceptionUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class CommonErrorHandler extends ResponseEntityExceptionHandler {

    @Resource
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public ErrorResponse handleServiceExceptions(ServiceException ex) {
        log.error("Service error, status: {}, uuid: {}, code: {}, message: {}, {}",
                ex.getStatus(),
                ex.getUuid(),
                ex.getCode(),
                ex.getMessage(),
                ex.formatProperties());
        return ErrorResponse.fromServiceException(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ErrorResponse handleMaxSizeException(MaxUploadSizeExceededException ex) {
        String uuid = UUID.randomUUID().toString();
        log.error("File upload error, uuid: {}, message: {}", uuid, ex.getMessage());

        return ErrorResponse.build(
                uuid,
                HttpStatus.BAD_REQUEST,
                "Maximum allowed file size exceeded"
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        String uuid = UUID.randomUUID().toString();
        log.error("Constraint violation error, uuid: {}, message: {}", uuid, ex.getMessage());
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        List<ValidationError> errors = constraintViolations.stream()
                .map(constraintViolation ->
                        new ValidationError(ErrorLevel.ERROR,
                                getField(constraintViolation),
                                getConstraintViolationMessage(constraintViolation)))
                .collect(Collectors.toList());

        return ErrorResponse.build(
                uuid,
                HttpStatus.BAD_REQUEST,
                "Invalid argument values",
                errors
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        String uuid = UUID.randomUUID().toString();
        log.error("Forbidden, uuid: {}, message: {}", uuid, ex.getMessage());
        return ErrorResponse.build(uuid, HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleCommonException(CommonException ex) {
        log.error("Common exception, uuid: {}, status: {}, message: {}",
                ex.getUuid(), ex.getStatus(), ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.build(ex.getUuid(),
                HttpStatus.valueOf(ex.getStatus()),
                ex.getMessage(),
                ex.getMethod(),
                ex.getPath(),
                ex.getTimestamp());

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrors(Exception ex) {
        if (ExceptionUtil.isReadTimeoutError(ex)) {
            return handleServiceTimeout((SocketTimeoutException) ex.getCause());
        }

        return handleInternalServiceError(ex);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request) {
        return handleBindingException(ex, ex.getBindingResult(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return handleBindingException(ex, ex.getBindingResult(), headers);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        String uuid = UUID.randomUUID().toString();
        log.error("Missing request param error, uuid: {}, message: {}", uuid, ex.getMessage());
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        var errorResponse = ErrorResponse.build(uuid, badRequest, ex.getMessage());

        return ResponseEntity.status(badRequest).headers(headers).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        String uuid = UUID.randomUUID().toString();
        log.error("Missing path variable error, uuid: {}, message: {}", uuid, ex.getMessage());
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        var errorResponse = ErrorResponse.build(uuid, badRequest, ex.getMessage());

        return ResponseEntity.status(badRequest).headers(headers).body(errorResponse);
    }

    private ResponseEntity<Object> handleBindingException(Throwable ex,
                                                          BindingResult bindingResult,
                                                          HttpHeaders headers) {
        String uuid = UUID.randomUUID().toString();
        log.error("Method argument not valid, uuid: {}, message: {}", uuid, ex.getMessage());
        List<ValidationError> errors = new ArrayList<>();
        errors.addAll(bindingResult.getFieldErrors().stream()
                .map(fieldError -> new ValidationError(ErrorLevel.ERROR, fieldError.getField(),
                        getErrorBindingMessage(fieldError)))
                .collect(Collectors.toList()));

        errors.addAll(bindingResult.getGlobalErrors().stream()
                .map(globalError -> new ValidationError(ErrorLevel.ERROR,
                        globalError.getObjectName(),
                        getErrorBindingMessage(globalError)))
                .collect(Collectors.toList()));

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        var errorResponse = ErrorResponse.build(
                uuid,
                badRequest,
                "Invalid Arguments",
                errors
        );

        return ResponseEntity.status(badRequest).headers(headers).body(errorResponse);
    }

    private String getErrorBindingMessage(ObjectError objectError) {
        String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
        return buildErrorMessage(message);
    }

    private static String getField(ConstraintViolation<?> constraintViolation) {
        String propertyPath = constraintViolation.getPropertyPath().toString();
        return propertyPath.substring(propertyPath.lastIndexOf(".") + 1);
    }

    private String getConstraintViolationMessage(ConstraintViolation constraintViolation) {
        return buildErrorMessage(constraintViolation.getMessage());
    }

    private String buildErrorMessage(String message) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(
                    message,
                    null,
                    locale);
        } catch (NoSuchMessageException exception) {
            return message;
        }
    }

    private ResponseEntity<ErrorResponse> handleServiceTimeout(SocketTimeoutException ex) {
        String uuid = UUID.randomUUID().toString();
        log.error("Service timeout error, uuid: {}, code: {}, message: {}",
                uuid, HttpStatus.GATEWAY_TIMEOUT, ex.getMessage());
        var errorResponse = ErrorResponse.build(
                uuid,
                HttpStatus.GATEWAY_TIMEOUT,
                "Service timeout error"
        );

        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(errorResponse);
    }

    private ResponseEntity<ErrorResponse> handleInternalServiceError(Exception ex) {
        String uuid = UUID.randomUUID().toString();
        log.error("Internal server error, uuid: {}, message: {}",
                uuid, ex.getMessage());
        var internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        var errorResponse = ErrorResponse.build(uuid, internalServerError, "Internal server error");

        return ResponseEntity.status(internalServerError).body(errorResponse);
    }

}