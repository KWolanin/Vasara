package com.kai.Vasara.service.chapter;

import com.kai.Vasara.model.chapter.ChapterDTO;
import com.kai.Vasara.repository.chapter.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetWholeChapterService {

    private final ChapterRepository chapterRepository;
    private final MapperService mapperService;

    @Autowired
    public GetWholeChapterService(ChapterRepository chapterRepository, MapperService mapperService) {
        this.chapterRepository = chapterRepository;
        this.mapperService = mapperService;
    }

    public int getChapterNumber(Long storyId) {
        return chapterRepository.countByStoryId(storyId);
    }

    @Transactional
    public ChapterDTO getWholeChapter(Long storyId, Long chapterNo) {
        return mapperService.from(chapterRepository.findByStoryIdAndChapterNo(storyId, chapterNo));
    }

    @Transactional
    public List<ChapterDTO> getChapters(Long storyId) {
        return chapterRepository.findChaptersDataByStoryId(storyId)
                .stream()
                .map(mapperService::from)
                .toList();
    }

}
