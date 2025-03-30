package com.kai.Vasara.model.author;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.model.story.StoryInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorInfo {
    private long id;
    private String username;
    private String email;
    private String description;
    private String login;
    private List<StoryInfo> stories;

    public static AuthorInfo from(Author author) {
        return new AuthorInfo(
              author.getId(),
              author.getUsername(),
              author.getEmail(),
              author.getDescription(),
              author.getLogin(), author.getStories().stream().filter(story -> !story.getChapters().isEmpty()).map(StoryInfo::from).toList());
    }
}
