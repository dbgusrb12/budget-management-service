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

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse applicationExceptionHandler(ApplicationException exception) {
        printErrorLog(exception);
        return new ErrorResponse(exception.getApplicationCode());
    }

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
