package com.kai.Vasara.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class FavouriteStoryDAO {

    private long id;
    private ZonedDateTime addedOn;
    private StoryDAO storyDAO;
    private AuthorDAO authorDAO;
}
