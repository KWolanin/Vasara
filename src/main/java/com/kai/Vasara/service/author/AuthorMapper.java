package com.kai.Vasara.service.author;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.model.author.AuthorDTO;
import com.kai.Vasara.service.story.StoryMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthorMapper {

    private final StoryMapper storyMapper;

    public AuthorMapper(StoryMapper storyMapper) {
        this.storyMapper = storyMapper;
    }

    public AuthorDTO from(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setLogin(author.getLogin());
        authorDTO.setUsername(author.getUsername());
        authorDTO.setEmail(author.getEmail());
        authorDTO.setDescription(author.getDescription());
        authorDTO.setStories(new ArrayList<>());
        author.getStories().forEach(story -> authorDTO.getStories().add(storyMapper.from(story)));
        return authorDTO;
    }

    public Author from(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setId(authorDTO.getId());
        author.setUsername(authorDTO.getUsername());
        author.setEmail(authorDTO.getEmail());
        author.setDescription(authorDTO.getDescription());
        author.setStories(new ArrayList<>());
        authorDTO.getStories().forEach(storyDAO -> author.getStories().add(storyMapper.from(storyDAO)));
        return author;
    }
}
