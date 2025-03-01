package com.kai.Vasara.exception.chapter;

import com.kai.Vasara.exception.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChapterExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorInfo> handleException(ChapterException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        switch (e.getChapterError()) {
            case CHAPTER_NOT_FOUND -> httpStatus = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(httpStatus).body(new ErrorInfo(e.getChapterError().getMessage()));
    }
}
