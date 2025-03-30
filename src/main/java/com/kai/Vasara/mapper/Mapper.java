package com.kai.Vasara.mapper;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.model.author.AuthorDTO;
import com.kai.Vasara.model.chapter.ChapterDTO;
import com.kai.Vasara.model.story.StoryDTO;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring", uses = MapperUtil.class)
public interface Mapper {

    Author authorDTOToAuthor(AuthorDTO authorDTO);

    AuthorDTO authorToAuthorDTO(Author author);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "joinAndAddQuotes")
    @Mapping(target = "fandoms", source = "fandoms", qualifiedByName = "joinAndAddQuotes")
    @Mapping(target = "allowComments", source = "comment")
    @Mapping(target = "allowGuestComments", source = "guestComment")
    @Mapping(target = "author", source = "authorId", qualifiedByName = "getAuthor")
    Story storyDTOToStory(StoryDTO storyDTO);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "splitAndRemoveQuotes")
    @Mapping(target = "fandoms", source = "fandoms", qualifiedByName = "splitAndRemoveQuotes")
    @Mapping(target = "authorName", source = "author.username")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "comment", source = "allowComments")
    @Mapping(target = "guestComment", source = "allowGuestComments")
    @Mapping(target = "chaptersNumber", source = "chapters", qualifiedByName = "countChapters")
    @Mapping(target = "chaptersTitles", source = "chapters", qualifiedByName = "getChapterTitles")
    StoryDTO storyToStoryDTO(Story story);

    @Mapping(target = "story", source = "storyId", qualifiedByName = "getStory")
    Chapter chapterDTOToChapter(ChapterDTO chapterDTO);
    @Mapping(target = "storyId", source = "story.id")
    ChapterDTO chapterToChapterDTO(Chapter chapter);

    @Named("splitAndRemoveQuotes")
    default List<String> splitAndRemoveQuotes(String input) {
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

    @Named("joinAndAddQuotes")
    default String joinAndAddQuotes(List<String> inputList) {
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

    @Named("countChapters")
    default int countChapters(List<Chapter> chapters) {
        return chapters.size();
    }

    @Named("getChapterTitles")
    default List<String> getChapterTitles(List<Chapter> chapters) {
        return chapters.stream()
                .map(c -> c.getChapterNo() + ". " + c.getChapterTitle())
                .toList();
    }

}
