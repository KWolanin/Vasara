package com.kai.Vasara.exception.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentException extends RuntimeException{

    private CommentError commentError;
}
