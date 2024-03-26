package com.hg.budget.was.core.exception;

import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.was.core.response.ErrorResponse;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        printErrorLog(exception);
        return new ErrorResponse(exception.getApplicationCode());
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

    public void printErrorLog(Exception exception) {
        String message = Arrays.stream(exception.getStackTrace())
            .filter(stackTraceElement -> stackTraceElement.getClassName().startsWith("com.hg.budget"))
            .map(stackTraceElement -> "\tat " + stackTraceElement)
            .collect(Collectors.joining("\n"));
        if (message.isBlank()) {
            exception.printStackTrace();
            return;
        }
        log.error(message);
    }
}
