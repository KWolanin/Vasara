package com.kai.Vasara.service.chapter;

import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.repository.chapter.ChapterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheService {

    private final ChapterRepository repository;

    public CacheService(ChapterRepository repository) {
        this.repository = repository;
    }


    @Cacheable(value = "chapterContent", key="{#storyId, #chapterNo}")
    public Chapter getSplitChapter(Long storyId, Long chapterNo) {
        return repository.findByStoryIdAndChapterNo(storyId, chapterNo);
    }
}
