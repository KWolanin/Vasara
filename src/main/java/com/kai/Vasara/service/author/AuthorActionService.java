package com.kai.Vasara.service.author;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface AuthorActionService<T> {

    Optional<T> check(long followingAuthorId, long followedAuthorId);

    boolean add(long followingAuthorId, long followedAuthorId);

    Boolean is(long followingAuthorId, long followedAuthorId);


}
