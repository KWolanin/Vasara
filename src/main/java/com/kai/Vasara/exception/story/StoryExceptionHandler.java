package com.kai.Vasara.exception.story;

import com.kai.Vasara.exception.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StoryExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handleException(StoryException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        switch (e.getStoryError()) {
            case STORY_NOT_FOUND, STORY_TO_READ_NOT_FOUND -> httpStatus = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getStoryError().getMessage()));
    }
}