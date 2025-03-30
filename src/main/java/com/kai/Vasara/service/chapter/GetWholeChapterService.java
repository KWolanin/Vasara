package com.kai.Vasara.service.chapter;

import com.kai.Vasara.mapper.Mapper;
import com.kai.Vasara.model.chapter.ChapterDTO;
import com.kai.Vasara.model.chapter.ChapterInfo;
import com.kai.Vasara.repository.chapter.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetWholeChapterService {

    private final ChapterRepository chapterRepository;
    private final Mapper mapper;

    @Autowired
    public GetWholeChapterService(ChapterRepository chapterRepository, Mapper mapper) {
        this.chapterRepository = chapterRepository;
        this.mapper = mapper;
    }

    public int getChapterNumber(Long storyId) {
        return chapterRepository.countByStoryId(storyId);
    }

    @Transactional
    public ChapterDTO getWholeChapter(Long storyId, Long chapterNo) {
        return mapper.chapterToChapterDTO(chapterRepository.findByStoryIdAndChapterNo(storyId, chapterNo));
    }

    @Transactional
    public List<ChapterInfo> getChaptersInfo(Long storyId) {
        return chapterRepository.findChaptersDataByStoryId(storyId)
                .stream()
                .map(ChapterInfo::from)
                .toList();
    }

}
