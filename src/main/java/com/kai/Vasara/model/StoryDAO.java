package com.kai.Vasara.model;

import com.kai.Vasara.service.AuthorService;
import com.kai.Vasara.service.ChapterService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Component
@NoArgsConstructor
public class StoryDAO {

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

    @Autowired
    private AuthorService authorService;
    @Autowired
    private ChapterService chapterService;

    private List<ChapterDAO> chapters;
    private AuthorDAO author;
}
