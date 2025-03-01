package com.kai.Vasara.exception.chapter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChapterException extends RuntimeException {

    private ChapterError chapterError;
}
