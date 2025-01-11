package com.kai.Vasara.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Criteria {

    private String title;
    private String author;
    private List<String> fandoms;
    private List<String> tags;
    private String description;

    public boolean areFieldsEmpty() {
        return (title == null || title.trim().isEmpty()) &&
                (author == null || author.trim().isEmpty()) &&
                (fandoms == null || fandoms.isEmpty()) &&
                (tags == null || tags.isEmpty()) &&
                (description == null || description.trim().isEmpty());
    }

}
