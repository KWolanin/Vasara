package com.kai.Vasara.mapper;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.repository.author.AuthorRepository;
import com.kai.Vasara.repository.story.StoryRepository;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {


    private final AuthorRepository authorRepository;
    private final StoryRepository storyRepository;

    public MapperUtil(AuthorRepository authorRepository, StoryRepository storyRepository) {
        this.authorRepository = authorRepository;
        this.storyRepository = storyRepository;
    }

    @Named("getAuthor")
    public Author findAuthorById(Long authorId) {
        if (authorId != null && authorId > 0) {
            return authorRepository.findById(authorId).orElse(null);
        }
        return null;
    }

    @Named("getStory")
    public Story findStoryById(Long storyId) {
        if (storyId != null && storyId > 0) {
            return storyRepository.findById(storyId).orElse(null);
        }
        return null;
    }
}
