package com.kai.Vasara.model;

import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@NoArgsConstructor
public class ChapterDAO {
    private long id;
    private int chapterNo;
    private String chapterTitle;
    @Lob
    private String content;
    private long storyId;
}
