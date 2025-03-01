package com.kai.Vasara.exception.story;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoryError {
    STORY_NOT_FOUND("Story was not found"),
    STORY_TO_READ_NOT_FOUND("There is no stories for read later");

    private final String message;

}
