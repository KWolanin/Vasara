package com.kai.Vasara.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "story")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "authorid")
    private long authorId;
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

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();
}
