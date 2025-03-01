package com.kai.Vasara.model.author;

import com.kai.Vasara.model.story.StoryDTO;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@Component
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthorDTO {

    private long id;
    private String username;
    private List<StoryDTO> stories;
    private String email;
    private String description;
}
