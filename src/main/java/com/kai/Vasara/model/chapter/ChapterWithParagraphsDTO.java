package com.kai.Vasara.model.chapter;

import com.kai.Vasara.model.story.StoryDTO;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterWithParagraphsDTO {
    private long id;
    private int chapterNo;
    private String chapterTitle;
    private List<ParagraphDTO> paragraphs;
    private long storyId;
    private ZonedDateTime published;
    private ZonedDateTime updated;
    private StoryDTO storyDTO;
    private boolean isNext;
    private boolean isPrevious;
}



