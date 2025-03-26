package com.kai.Vasara.model.story;

import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.model.StoryRating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoryInfo {

    private long id;
    private String authorName;
    private long authorId;
    private String description;
    private String title;
    private List<String> tags;
    private List<String> fandoms;
    private boolean finished;
    private ZonedDateTime publishDt;
    private ZonedDateTime updateDt;
    private int chaptersNumber;
    private List<String> chaptersTitles;
    private StoryRating rating = StoryRating.KIDS;
    private boolean comment;
    private boolean guestComment;

    public static StoryInfo from(Story story) {

        List<Chapter> chapters = story.getChapters();
        chapters.size();

        return new StoryInfo(story.getId(),
                story.getAuthor().getUsername(),
                story.getAuthor().getId(),
                story.getDescription(),
                story.getTitle(),
                splitAndRemoveQuotes(story.getTags()),
                splitAndRemoveQuotes(story.getFandoms()),
                story.isFinished(),
                story.getPublishDt(),
                story.getUpdateDt(),
                chapters.size(),
                chapters.stream().map(Chapter::getChapterTitle).toList(),
                story.getRating(),
                story.isAllowComments(),
                story.isAllowGuestComments());
    }


    public static List<String> splitAndRemoveQuotes(String input) {
        List<String> resultList = new ArrayList<>();
        if (StringUtils.hasLength(input) && !noTag(input)) {
            input = input.substring(1, input.length() - 1);
            String[] parts = input.split(",");
            for (String part : parts) {
                resultList.add(part.replace("\"", "").trim());
            }
        }
        return resultList;
    }

    private static boolean noTag(String tag) {
        return "[]".equals(tag) || tag.isEmpty();
    }
}
