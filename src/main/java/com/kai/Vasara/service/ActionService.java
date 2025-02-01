package com.kai.Vasara.service;

import com.kai.Vasara.model.StoryDAO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ActionService<T> {

    boolean add(long authorId, long storyId);

    Optional<T> check(long authorId, long storyId);

    boolean is(long authorId, long storyId);

    Page<StoryDAO> get(int page, int size, long id);
}
