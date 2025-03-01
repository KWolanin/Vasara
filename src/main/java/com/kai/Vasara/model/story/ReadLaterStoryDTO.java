package com.kai.Vasara.model.story;

import com.kai.Vasara.model.author.AuthorDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class ReadLaterStoryDTO {

    private long id;
    private ZonedDateTime addedOn;
    private StoryDTO storyDTO;
    private AuthorDTO authorDTO;
}
