package com.kai.Vasara.service.chapter;

import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.mapper.Mapper;
import com.kai.Vasara.model.chapter.ChapterDTO;
import com.kai.Vasara.model.chapter.ChapterInfo;
import com.kai.Vasara.repository.chapter.ChapterRepository;
import com.kai.Vasara.repository.story.StoryRepository;
import com.kai.Vasara.service.EmailService;
import com.kai.Vasara.service.comment.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EditChapterService {

    private final ChapterRepository chapterRepository;
    private final EmailService emailService;
    private final CommentService commentService;
    private final StoryRepository storyRepository;
    private final Mapper mapper;

    public EditChapterService(ChapterRepository chapterRepository, EmailService emailService, CommentService commentService, StoryRepository storyRepository, Mapper mapper) {
        this.chapterRepository = chapterRepository;
        this.emailService = emailService;
        this.commentService = commentService;
        this.storyRepository = storyRepository;
        this.mapper = mapper;
    }

    @Caching(evict = {
            @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true),
            @CacheEvict(value = "chapterContent", key = "#chapterDTO.storyId.longValue()", allEntries = true)
    })
    @Transactional
    public void saveChapter(ChapterDTO chapterDTO) {
        Chapter chapter = mapper.chapterDTOToChapter(chapterDTO);
        chapterRepository.save(chapter);
        if (chapter.getStory() != null) {
            Story story = chapter.getStory();
            story.setUpdateDt(chapter.getUpdated());
            storyRepository.save(story);
        }
        if (chapter.getChapterNo() == 1) {
            emailService.sendMessageAboutNewStoryToQueue(chapter.getStory());
        } else {
            emailService.sendMessageAboutNewChapterToQueue(chapter);
        }
    }


    @CacheEvict(value = "chapterContent", key = "{#chapterDTO.storyId.longValue(), #chapterDTO.chapterNo.longValue()}")
    public void editChapter(ChapterDTO chapterDTO) {
        Optional<Chapter> chapter = chapterRepository.findById(chapterDTO.getId());
        if (chapter.isPresent()) {
            Chapter c = chapter.get();
            c.setUpdated(chapterDTO.getUpdated());
            c.setChapterTitle(chapterDTO.getChapterTitle());
            c.setContent(chapterDTO.getContent());
            chapterRepository.save(c);
        }
    }

    public void deleteChaptersForStory(Long storyId) {
        chapterRepository.deleteAllByStoryId(storyId);
    }

    @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true)
    public void deleteChapter(Long id) {
        commentService.deleteByChapterId(id);
        chapterRepository.deleteById(id);
    }

    @Transactional
    public void editChaptersOrder(List<ChapterInfo> chapters) {
        List<Long> chapterIds = chapters.stream()
                .map(ChapterInfo::getId)
                .collect(Collectors.toList());
        List<Chapter> existingChapters = chapterRepository.findAllById(chapterIds);
        existingChapters.forEach(existingChapter -> chapters.stream()
                .filter(chapterInfo -> chapterInfo.getId() == (existingChapter.getId()))
                .findFirst()
                .ifPresent(chapterInfo -> {
                    existingChapter.setChapterTitle(chapterInfo.getChapterTitle());
                    existingChapter.setChapterNo(chapterInfo.getChapterNo());
                }));
        chapterRepository.saveAll(existingChapters);
    }

}
