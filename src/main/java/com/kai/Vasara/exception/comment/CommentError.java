package com.kai.Vasara.exception.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentError {
    PARENT_NOT_FOUND("Parent comment not found");

    private final String message;

}
