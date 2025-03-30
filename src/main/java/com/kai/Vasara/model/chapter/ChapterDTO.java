package com.kai.Vasara.model.chapter;

import com.kai.Vasara.model.story.StoryDTO;
import jakarta.persistence.Lob;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterDTO {
    private long id;
    private int chapterNo;
    private String chapterTitle;
    @Lob
    private String content;
    private long storyId;
    private ZonedDateTime published;
    private ZonedDateTime updated;
}
