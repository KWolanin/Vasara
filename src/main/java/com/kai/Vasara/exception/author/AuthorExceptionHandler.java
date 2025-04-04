package com.kai.Vasara.exception.author;

import com.kai.Vasara.exception.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthorExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handleException(AuthorException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        switch (e.getAuthorError()) {
            case AUTHOR_LOGIN_EXISTS, AUTHOR_USERNAME_EXISTS,
                 AUTHOR_EMAIL_EXISTS, CANNOT_FOLLOW_ITSELF -> httpStatus = HttpStatus.CONFLICT;
            case AUTHOR_EMAIL_INCORRECT, AUTHOR_INVALID_CREDENTIALS,
                 AUTHOR_INVALID_DESCRIPTION, AUTHOR_INVALID_PASSWORD,
                 AUTHOR_INVALID_EMAIL, AUTHOR_INVALID_USERNAME -> httpStatus = HttpStatus.BAD_REQUEST;
            case AUTHOR_NOT_FOUND, AUTHOR_DETAILS_NOT_FOUND -> httpStatus = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getAuthorError().getMessage()));
    }
}
