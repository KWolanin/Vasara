package com.kai.Vasara.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoryError {
    STORY_NOT_FOUND("Story was not found");

    private final String message;

}
