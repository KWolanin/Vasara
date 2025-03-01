package com.kai.Vasara.entity.chapter;

import com.kai.Vasara.entity.story.Story;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Table(name = "chapter")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chapterno")
    private int chapterNo;
    @Column(name = "chaptertitle")
    private String chapterTitle;
    @Lob
    private String content;
    @Column(name = "published")
    private ZonedDateTime published;
    @Column(name = "updated")
    private ZonedDateTime updated;
    @ManyToOne
    @JoinColumn(name = "storyid", nullable = false)
    private Story story;

    public Chapter(Long id, int chapterNo, String chapterTitle, String content, Long storyId, ZonedDateTime published, ZonedDateTime updated) {
        this.id = id;
        this.chapterNo = chapterNo;
        this.chapterTitle = chapterTitle;
        this.content = content;
        this.story = new Story();
        this.story.setId(storyId);
        this.published = published;
        this.updated = updated;
    }
}
