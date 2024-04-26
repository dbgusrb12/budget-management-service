package com.hg.budget.was.core.exception;

import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.was.core.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 인증이 실패했을 때 발생하는 에러
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse authenticationExceptionHandler(AuthenticationException exception) {
        printErrorLog(exception);
        return new ErrorResponse(exception.getApplicationCode());
    }

    /**
     * 인증은 완료되었지만, 권한이 없을 때 발생하는 에러
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse accessDeniedExceptionHandler(AccessDeniedException exception) {
        printErrorLog(exception);
        return new ErrorResponse(exception.getApplicationCode());
    }

    /**
     * Spring Handler 등록 되어 있지 않을 때 발생하는 에러 (핸들러가 없다고 표시하기보단 권한이 없다고 표시하는게 보안상 더 좋기 때문에 FORBIDDEN 에러로 처리)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse noResourceFoundExceptionHandler(NoResourceFoundException exception) {
        printErrorLog(exception);
        return new ErrorResponse(ApplicationCode.ACCESS_DENIED_AUTHENTICATION);
    }

    /**
     * Application 에서 발생하는 에러
     */
    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse applicationExceptionHandler(ApplicationException exception) {
        if (exception.getLogMessage() != null) {
            log.error("Application Exception Log Message : {}", exception.getLogMessage());
        }
        printErrorLog(exception);
        return new ErrorResponse(exception.getApplicationCode());
    }

    /**
     * Controller 에서 @Valid @RequestBody 어노테이션 검증 시 발생하는 에러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        final List<ValidationError> validationErrors = exception.getBindingResult().getFieldErrors().stream()
            .map(ValidationError::from)
            .toList();
        log.error("error: {} message : {}\nfield: {}", exception.getClass().getSimpleName(), exception.getMessage(), validationErrors);
        return new ErrorResponse(ApplicationCode.BAD_REQUEST);
    }

    /**
     * Controller 에서 @Validated 어노테이션 검증 시 발생하는 에러
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationExceptionHandler(ConstraintViolationException exception) {
        final List<ValidationError> validationErrors = exception.getConstraintViolations().stream()
            .map(ValidationError::from)
            .toList();
        log.error("error: {} message : {}\nfield: {}", exception.getClass().getSimpleName(), exception.getMessage(), validationErrors);
        return new ErrorResponse(ApplicationCode.BAD_REQUEST);
    }

    /**
     * 그 외의 에러
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exceptionHandler(Exception exception) {
        printErrorLog(exception);
        return new ErrorResponse(ApplicationCode.UNKNOWN_SERVER_ERROR);
    }

    private void printErrorLog(Exception exception) {
        final String message = Arrays.stream(exception.getStackTrace())
            .filter(stackTraceElement -> stackTraceElement.getClassName().startsWith("com.hg.budget"))
            .map(stackTraceElement -> "\tat " + stackTraceElement)
            .collect(Collectors.joining("\n"));
        if (message.isBlank()) {
            log.error("not inner package exception", exception);
            return;
        }
        log.error("error: {} message : {}\nstacktrace: {}", exception.getClass().getSimpleName(), exception.getMessage(), message);
    }

    private record ValidationError(String field, String message) {

        private static ValidationError from(ConstraintViolation<?> constraintViolation) {
            return new ValidationError(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        }

        private static ValidationError from(FieldError fieldError) {
            return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
