package com.kai.Vasara.model.comment;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;
    private Long storyId;
    private Long chapterId;
    private String content;
    private Long parentId;
    private String name;
    private String email;
    private List<CommentDTO> replies;
    private ZonedDateTime createdAt;

}
