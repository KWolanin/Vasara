package com.kai.Vasara.entity.author;

import com.kai.Vasara.entity.story.Story;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Table(name = "author")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private String username;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Story> stories;

    @Email
    private String email;

    private String description;

    @OneToMany(mappedBy = "followingAuthor")
    private List<FollowAuthor> following;

    @OneToMany(mappedBy = "followedAuthor")
    private List<FollowAuthor> followers;

}
