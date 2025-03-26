package com.kai.Vasara.model.story;

import com.kai.Vasara.model.chapter.ChapterDTO;
import com.kai.Vasara.model.StoryRating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Component
@NoArgsConstructor
public class StoryDTO {

    private long id;
    private String authorName;
    private long authorId;
    private String description;
    private String title;
    private List<String> tags;
    private List<String> fandoms;
    private boolean finished;
    private ZonedDateTime publishDt;
    private ZonedDateTime updateDt;
    private int chaptersNumber;
    private List<ChapterDTO> chapters; // todo: check if can be removed safely
    private List<String> chaptersTitles;
    private StoryRating rating = StoryRating.KIDS;
    private boolean comment;
    private boolean guestComment;
}
