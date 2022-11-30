package ru.practicum.explorewithme.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError exceptionServerHandler(final IllegalArgumentException e) {
        return new ApiError(null, e.getLocalizedMessage(),
                e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError validationExceptionData(final EmptyResultDataAccessException e) {
        return new ApiError(null, e.getLocalizedMessage(),
                e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFoundExceptionHandler(final NotFoundException e) {
        return new ApiError(null, e.getLocalizedMessage(),
                e.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError notValidArgExceptionHandler(final MethodArgumentNotValidException e) {
        return new ApiError(null, e.getLocalizedMessage(),
                e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler(IllegalCallerException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError illegalCallExceptionHandler(final IllegalCallerException e) {
        return new ApiError(null, e.getLocalizedMessage(),
                e.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError violationExceptionHandler(final ConstraintViolationException e) {
        return new ApiError(e.getStackTrace(), e.getLocalizedMessage(),
                e.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError validationExceptionHandler(final ValidationException e) {
        return new ApiError(null, e.getLocalizedMessage(),
                e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError requestParamExceptionHandler(final MissingServletRequestParameterException e) {
        return new ApiError(null, e.getLocalizedMessage(),
                e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }
}
