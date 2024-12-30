package com.kai.Vasara.entity;

import jakarta.persistence.*;
import lombok.*;

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
    //private long authorId;
    @Column(name = "storyid")
    private long storyId;
}
