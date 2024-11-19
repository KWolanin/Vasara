package com.kai.Vasara.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.kai.Vasara.entity.Author;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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

//    @ManyToOne
//    private AuthorDAO author;
//    @ManyToOne
//    private StoryDAO story;

    private long storyId;
}
