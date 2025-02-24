package com.kai.Vasara.service;

import com.kai.Vasara.entity.Chapter;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.exception.ChapterError;
import com.kai.Vasara.exception.ChapterException;
import com.kai.Vasara.model.ChapterDAO;
import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.repository.ChapterRepository;
import com.kai.Vasara.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Component
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final StoryRepository storyRepository;
    private final EmailService emailService;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository, StoryRepository storyRepository, EmailService emailService) {
        this.chapterRepository = chapterRepository;
        this.storyRepository = storyRepository;
        this.emailService = emailService;
    }

    public ChapterDAO getChapter(Long id) {
        return chapterRepository.findById(id)
                .map(this::from)
                .orElseThrow(() -> new ChapterException(ChapterError.CHAPTER_NOT_FOUND));
    }

    @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true)
    public void saveChapter(ChapterDAO chapterDAO) {
            Chapter chapter = from(chapterDAO);
            if (chapter.getStory() != null) {
                Story story = chapter.getStory();
                story.setUpdateDt(chapter.getUpdated());
            }
            chapterRepository.save(chapter);
            emailService.sendMessageToQueue(chapter);
    }

    public int getChapterNumber(Long storyId) {
        return chapterRepository.countByStoryId(storyId);
    }

    @Transactional
    public ChapterDAO getChapterByStoryIdAndNumber(Long storyId, Long chapterNo) {
        return chapterRepository.findByStoryIdAndChapterNo(storyId, chapterNo)
                .map(this::from)
                .orElseThrow(() -> new ChapterException(ChapterError.CHAPTER_NOT_FOUND));
    }

    public Chapter from(ChapterDAO chapterDAO) {
        Chapter chapter = new Chapter();
        chapter.setId(chapterDAO.getId());
        chapter.setChapterNo(chapterDAO.getChapterNo());
        chapter.setChapterTitle(chapterDAO.getChapterTitle());
        chapter.setContent(chapterDAO.getContent());
        chapter.setPublished(chapterDAO.getPublished());
        chapter.setUpdated(chapterDAO.getUpdated());
        if (chapterDAO.getStoryId() > 0) {
            Optional<Story> st = storyRepository.findById(chapterDAO.getStoryId());
            st.ifPresent(chapter::setStory);
        }
        return chapter;
    }

    public ChapterDAO from(Chapter chapter) {
        ChapterDAO chapterDAO = new ChapterDAO();
        chapterDAO.setId(chapter.getId());
        chapterDAO.setChapterNo(chapter.getChapterNo());
        chapterDAO.setChapterTitle(chapter.getChapterTitle());
        chapterDAO.setContent(chapter.getContent());
        chapterDAO.setPublished(chapter.getPublished());
        chapterDAO.setUpdated(chapter.getUpdated());
        if (chapter.getStory() != null) {
            StoryDAO storyDAO = new StoryDAO();
            storyDAO.setId(chapter.getStory().getId());
            storyDAO.setTitle(chapter.getStory().getTitle());
            chapterDAO.setStoryDAO(storyDAO);
            chapterDAO.setStoryId(storyDAO.getId());
        }
        return chapterDAO;
    }

    @Transactional
    public Boolean checkIsNextOrPrevious(Long storyId, Long chapterNo) {
        return chapterRepository.findByStoryIdAndChapterNo(storyId, chapterNo).isPresent();
    }

    public void deleteChaptersForStory(Long storyId) {
        chapterRepository.deleteAllByStoryId(storyId);
    }

    @Transactional
    public List<ChapterDAO> getChapters(Long storyId) {
        return chapterRepository.findChaptersDataByStoryId(storyId)
                .stream()
                .map(this::from)
                .toList();
    }

    @Transactional
    public void editChaptersOrder(List<ChapterDAO> chapters) {
            List<Long> chapterIds = chapters.stream()
                    .map(ChapterDAO::getId)
                    .collect(Collectors.toList());

            List<Chapter> existingChapters = chapterRepository.findAllById(chapterIds);

            existingChapters.forEach(existingChapter -> chapters.stream()
                    .filter(chapterDAO -> chapterDAO.getId() == (existingChapter.getId()))
                    .findFirst()
                    .ifPresent(chapterDAO -> {
                        existingChapter.setChapterTitle(chapterDAO.getChapterTitle());
                        existingChapter.setChapterNo(chapterDAO.getChapterNo());
                    }));
            chapterRepository.saveAll(existingChapters);
    }

    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }
}
