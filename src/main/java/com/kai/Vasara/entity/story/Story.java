package com.kai.Vasara.entity.story;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.model.StoryRating;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "story", indexes = {
        @Index(name = "idx_story_title", columnList = "title"),
        @Index(name = "idx_story_tags", columnList = "tags"),
        @Index(name = "idx_story_fandoms", columnList = "fandom"),
        @Index(name = "idx_story_author", columnList = "authorid"),
        @Index(name = "idx_story_rating", columnList = "rating"),
        @Index(name = "idx_story_publishdt", columnList = "publishdt")
})@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private String description;
    private String title;
    private String tags;
    @Column(name = "fandom")
    private String fandoms;
    private boolean finished;
    @Column(name = "publishdt")
    private ZonedDateTime publishDt;
    @Column(name = "updatedt")
    private ZonedDateTime updateDt;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "authorid")
    private Author author;

    @Enumerated(EnumType.STRING)
    private StoryRating rating = StoryRating.KIDS;

    @Column(name = "comment")
    @ColumnDefault("true")
    private boolean allowComments;

    @Column(name = "guest_comment")
    @ColumnDefault("true")
    private boolean allowGuestComments;
}
