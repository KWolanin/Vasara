package com.kai.Vasara.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@Component
@NoArgsConstructor
public class AuthorDAO {

    private long id;
    private String username;

    private List<StoryDAO> stories;
    private String email;

}
