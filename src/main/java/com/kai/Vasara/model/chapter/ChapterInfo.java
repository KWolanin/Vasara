package com.kai.Vasara.model.chapter;

import com.kai.Vasara.entity.chapter.Chapter;
import lombok.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChapterInfo {

    private long id;
    private int chapterNo;
    private String chapterTitle;
    private long storyId;
    private ZonedDateTime published;
    private ZonedDateTime updated;

    public static ChapterInfo from(Chapter chapter) {
        return new ChapterInfo(
                chapter.getId(),
                chapter.getChapterNo(),
                chapter.getChapterTitle(),
                chapter.getStory().getId(),
                chapter.getPublished(),
                chapter.getUpdated()
        );
    }
}
