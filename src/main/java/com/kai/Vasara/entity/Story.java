package com.kai.Vasara.entity;

import com.kai.Vasara.model.Rating;
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

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "authorid")
    private Author author;

    @Enumerated(EnumType.STRING)
    private Rating rating = Rating.KIDS;
}
