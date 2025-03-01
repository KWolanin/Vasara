package com.kai.Vasara.service.story;

import com.kai.Vasara.model.story.StoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface StoryActionService<T> {

    boolean add(long authorId, long storyId);

    Optional<T> check(long authorId, long storyId);

    boolean is(long authorId, long storyId);

    Page<StoryDTO> get(int page, int size, long id);

    void delete(long id);

    int count(long id);
}
