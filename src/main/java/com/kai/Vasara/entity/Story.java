package com.kai.Vasara.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Table(name = "story")
@Entity
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long authorId;
    private String description;
    private String title;
    private List<String> tags;
    private List<String> fandoms;
    private boolean finished;
    private ZonedDateTime publishDt;
    private ZonedDateTime updateDt;
}
