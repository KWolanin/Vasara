package com.kai.Vasara.service.story;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.model.story.StoryDTO;
import com.kai.Vasara.service.author.AuthorService;
import com.kai.Vasara.service.chapter.GetWholeChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class StoryMapper {

    // todo: temporary solution; implement mapstruck after refactoring
    private final GetWholeChapterService getWholeChapterService;
    private final AuthorService authorService;

    public StoryMapper(GetWholeChapterService getWholeChapterService, @Lazy AuthorService authorService) {
        this.getWholeChapterService = getWholeChapterService;
        this.authorService = authorService;
    }

    public StoryDTO from(Story story) {
        StoryDTO s = new StoryDTO();
        s.setId(story.getId());
        s.setDescription(story.getDescription());
        s.setTitle(story.getTitle());
        s.setTags(splitAndRemoveQuotes(story.getTags()));
        s.setFandoms(splitAndRemoveQuotes(story.getFandoms()));
        s.setFinished(story.isFinished());
        s.setPublishDt(story.getPublishDt());
        s.setUpdateDt(story.getUpdateDt());
        s.setComment(story.isAllowComments());
        s.setGuestComment(story.isAllowGuestComments());
        s.setChaptersNumber(getWholeChapterService.getChapterNumber(story.getId()));
        s.setRating(story.getRating());
        if (story.getAuthor() != null) {
            s.setAuthorId(story.getAuthor().getId());
            s.setAuthorName(story.getAuthor().getUsername());
        }
        return s;
    }

    public Story from(StoryDTO storyDTO) {
        Story story = new Story();
        story.setDescription(storyDTO.getDescription());
        story.setTitle(storyDTO.getTitle());
        updateStoryTagsAndFandoms(story, storyDTO);
        story.setFinished(storyDTO.isFinished());
        story.setPublishDt(storyDTO.getPublishDt());
        story.setUpdateDt(storyDTO.getUpdateDt());
        story.setRating(storyDTO.getRating());
        story.setAllowComments(storyDTO.isComment());
        story.setAllowGuestComments(storyDTO.isGuestComment());

        if (storyDTO.getAuthorId() > 0) {
            Optional<Author> author = authorService.find(storyDTO.getAuthorId());
            author.ifPresent(story::setAuthor);
        }

        return story;
    }

    public void updateStoryTagsAndFandoms(Story story, StoryDTO storyDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (Objects.nonNull(storyDTO.getTags())) {
                String tagsJson = objectMapper.writeValueAsString(storyDTO.getTags());
                story.setTags(tagsJson);
            } else story.setTags("[\"\"]");
            if (Objects.nonNull(storyDTO.getFandoms())) {
                String fandomsJson = objectMapper.writeValueAsString(storyDTO.getFandoms());
                story.setFandoms(fandomsJson);
            } else story.setFandoms("[\"\"]");
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new RuntimeException("Error serializing to JSON", e);
        }
    }


    public List<String> splitAndRemoveQuotes(String input) {
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

    public String joinAndAddQuotes(List<String> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            return "[]";
        }
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < inputList.size(); i++) {
            result.append("\"").append(inputList.get(i)).append("\"");
            if (i < inputList.size() - 1) {
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
    }

}
