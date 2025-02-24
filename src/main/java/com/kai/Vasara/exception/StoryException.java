package com.kai.Vasara.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoryException extends RuntimeException {

    private StoryError storyError;
}
