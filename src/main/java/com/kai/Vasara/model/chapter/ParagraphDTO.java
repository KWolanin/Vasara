package com.kai.Vasara.model.chapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
public class ParagraphDTO {
    private int id;
    private String content;
}
