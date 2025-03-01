package com.kai.Vasara.entity.story;

import com.kai.Vasara.entity.author.Author;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Table(name = "following_story")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FollowStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @Column(name = "added_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime addedAt;
}
