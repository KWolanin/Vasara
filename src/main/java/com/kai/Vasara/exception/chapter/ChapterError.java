package com.kai.Vasara.exception.chapter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChapterError {
    CHAPTER_NOT_FOUND("Chapter was not found");

    private final String message;

}
