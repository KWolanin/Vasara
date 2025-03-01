package com.kai.Vasara.exception.author;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthorError {
    AUTHOR_LOGIN_EXISTS("User with this login already exists"),
    AUTHOR_USERNAME_EXISTS("User with this username already exists"),
    AUTHOR_EMAIL_EXISTS("User with this email already exists"),
    AUTHOR_EMAIL_INCORRECT("Email is incorrect"),
    AUTHOR_NOT_FOUND("User not found"),
    AUTHOR_INVALID_CREDENTIALS("Invalid login or password"),
    AUTHOR_INVALID_EMAIL("Email is invalid"),
    AUTHOR_INVALID_PASSWORD("Password is invalid"),
    AUTHOR_INVALID_DESCRIPTION("Description is invalid"),
    AUTHOR_INVALID_USERNAME("Username is invalid"),
    AUTHOR_DETAILS_NOT_FOUND("Details not found"),
    CANNOT_FOLLOW_ITSELF("User cannot follow themself");
    private final String message;
}
