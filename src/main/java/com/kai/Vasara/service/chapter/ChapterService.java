package com.kai.Vasara.service.chapter;

import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.exception.chapter.ChapterError;
import com.kai.Vasara.exception.chapter.ChapterException;
import com.kai.Vasara.model.chapter.ChapterDTO;
import com.kai.Vasara.model.story.StoryDTO;
import com.kai.Vasara.repository.chapter.ChapterRepository;
import com.kai.Vasara.repository.story.StoryRepository;
import com.kai.Vasara.service.EmailService;
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

    public ChapterDTO getChapter(Long id) {
        return chapterRepository.findById(id)
                .map(this::from)
                .orElseThrow(() -> new ChapterException(ChapterError.CHAPTER_NOT_FOUND));
    }

    @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true)
    public void saveChapter(ChapterDTO chapterDTO) {
            Chapter chapter = from(chapterDTO);
            if (chapter.getStory() != null) {
                Story story = chapter.getStory();
                story.setUpdateDt(chapter.getUpdated());
            }
            chapterRepository.save(chapter);
            if (chapter.getChapterNo() == 1) {
                emailService.sendMessageAboutNewStoryToQueue(chapter.getStory());
            } else {
                emailService.sendMessageAboutNewChapterToQueue(chapter);
            }
    }

    public int getChapterNumber(Long storyId) {
        return chapterRepository.countByStoryId(storyId);
    }

    @Transactional
    public ChapterDTO getChapterByStoryIdAndNumber(Long storyId, Long chapterNo) {
        return chapterRepository.findByStoryIdAndChapterNo(storyId, chapterNo)
                .map(this::from)
                .orElseThrow(() -> new ChapterException(ChapterError.CHAPTER_NOT_FOUND));
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
            chapterDTO.setStoryDTO(storyDTO);
            chapterDTO.setStoryId(storyDTO.getId());
        }
        return chapterDTO;
    }

    @Transactional
    public Boolean checkIsNextOrPrevious(Long storyId, Long chapterNo) {
        return chapterRepository.findByStoryIdAndChapterNo(storyId, chapterNo).isPresent();
    }

    public void deleteChaptersForStory(Long storyId) {
        chapterRepository.deleteAllByStoryId(storyId);
    }

    @Transactional
    public List<ChapterDTO> getChapters(Long storyId) {
        return chapterRepository.findChaptersDataByStoryId(storyId)
                .stream()
                .map(this::from)
                .toList();
    }

    @Transactional
    public void editChaptersOrder(List<ChapterDTO> chapters) {
            List<Long> chapterIds = chapters.stream()
                    .map(ChapterDTO::getId)
                    .collect(Collectors.toList());

            List<Chapter> existingChapters = chapterRepository.findAllById(chapterIds);

            existingChapters.forEach(existingChapter -> chapters.stream()
                    .filter(chapterDTO -> chapterDTO.getId() == (existingChapter.getId()))
                    .findFirst()
                    .ifPresent(chapterDTO -> {
                        existingChapter.setChapterTitle(chapterDTO.getChapterTitle());
                        existingChapter.setChapterNo(chapterDTO.getChapterNo());
                    }));
            chapterRepository.saveAll(existingChapters);
    }

    @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true)
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }
}
