package com.kai.Vasara.service.chapter;

import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.model.chapter.ChapterDTO;
import com.kai.Vasara.model.story.StoryDTO;
import com.kai.Vasara.repository.story.StoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChapterMapper {

    // temporary solution; todo: implement mapstruck
    private final StoryRepository storyRepository;

    public ChapterMapper(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public Chapter from(ChapterDTO chapterDTO) {
        Chapter chapter = new Chapter();
        chapter.setId(chapterDTO.getId());
        chapter.setChapterNo(chapterDTO.getChapterNo());
        chapter.setChapterTitle(chapterDTO.getChapterTitle());
        chapter.setContent(chapterDTO.getContent());
        chapter.setPublished(chapterDTO.getPublished());
        chapter.setUpdated(chapterDTO.getUpdated());
        if (chapterDTO.getStoryId() > 0) {
            Optional<Story> st = storyRepository.findById(chapterDTO.getStoryId());
            st.ifPresent(chapter::setStory);
        }
        return chapter;
    }

    public ChapterDTO from(Chapter chapter) {
        ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setId(chapter.getId());
        chapterDTO.setChapterNo(chapter.getChapterNo());
        chapterDTO.setChapterTitle(chapter.getChapterTitle());
        chapterDTO.setContent(chapter.getContent());
        chapterDTO.setPublished(chapter.getPublished());
        chapterDTO.setUpdated(chapter.getUpdated());
        if (chapter.getStory() != null) {
            StoryDTO storyDTO = new StoryDTO();
            storyDTO.setId(chapter.getStory().getId());
            storyDTO.setTitle(chapter.getStory().getTitle());
            storyDTO.setAuthorName(chapter.getStory().getAuthor().getLogin());
            storyDTO.setAuthorId(chapter.getStory().getAuthor().getId());
            chapterDTO.setStoryDTO(storyDTO);
            chapterDTO.setStoryId(storyDTO.getId());
        }
        return chapterDTO;
    }
}
