package com.kai.Vasara.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChapterException extends RuntimeException {

    private ChapterError chapterError;
}
