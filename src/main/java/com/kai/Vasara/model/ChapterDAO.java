package com.kai.Vasara.model;

import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterDAO {
    private long id;
    private int chapterNo;
    private String chapterTitle;
    @Lob
    private String content;
    private long storyId;
    private ZonedDateTime published;
    private ZonedDateTime updated;

    private StoryDAO storyDAO;
}
