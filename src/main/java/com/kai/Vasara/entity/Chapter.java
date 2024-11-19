package com.kai.Vasara.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "chapter")
@Entity
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int chapterNo;
    private String chapterTitle;
    @Lob
    private String content;
    //private long authorId;
    private long storyId;
}
