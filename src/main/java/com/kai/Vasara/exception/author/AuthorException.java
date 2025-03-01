package com.kai.Vasara.exception.author;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorException extends RuntimeException{

    private AuthorError authorError;
}
