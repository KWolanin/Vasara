package com.kai.Vasara.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@Component
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AuthorDAO {

    private long id;
    private String username;

    private List<StoryDAO> stories;
    private String email;

}
