package com.kai.Vasara.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Table(name = "story")
@Entity
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
}
