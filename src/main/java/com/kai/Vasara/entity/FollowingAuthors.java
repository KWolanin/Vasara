package com.kai.Vasara.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Table(name = "following_author")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FollowingAuthors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "following_author_id", nullable = false)
    private Author followingAuthor;

    @ManyToOne
    @JoinColumn(name = "followed_author_id", nullable = false)
    private Author followedAuthor;

    @Column(name = "added_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime addedAt;
}
