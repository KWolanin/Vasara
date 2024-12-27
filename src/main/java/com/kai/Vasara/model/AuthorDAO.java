package com.kai.Vasara.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@NoArgsConstructor
public class AuthorDAO {

    private long id;
    private String username;
}
