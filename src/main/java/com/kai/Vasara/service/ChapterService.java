package com.kai.Vasara.service;

import com.kai.Vasara.entity.Chapter;
import com.kai.Vasara.model.ChapterDAO;
import com.kai.Vasara.repository.ChapterRepository;
import com.kai.Vasara.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
   // private final StoryService storyService;
   // private final AuthorService authorService;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    public List<ChapterDAO> getAll() {
        return chapterRepository.findAll()
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    public ChapterDAO getChapter(Long id) {
         Optional<Chapter> opt = chapterRepository.findById(id);
         if (opt.isPresent()) return from(opt.get());
        else return new ChapterDAO();
    }

    public Boolean saveChapter(ChapterDAO chapterDAO) {
        try {
            chapterRepository.save(from(chapterDAO));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getChapterNumber(Long storyId) {
        return chapterRepository.countByStoryId(storyId);
    }

    @Transactional
    public ChapterDAO getChapterByStoryIdAndNumber(Long storyId, Long chapterNo) {
        return from(chapterRepository.findByStoryIdAndNumberNo(storyId, chapterNo));
    }

    public Chapter from(ChapterDAO chapterDAO) {
        Chapter chapter = new Chapter();
        chapter.setId(chapterDAO.getId());
        chapter.setChapterNo(chapterDAO.getChapterNo());
        chapter.setChapterTitle(chapterDAO.getChapterTitle());
        chapter.setContent(chapterDAO.getContent());
        chapter.setStoryId(chapterDAO.getStoryId());
        return chapter;
    }

    public ChapterDAO from(Chapter chapter) {
        ChapterDAO chapterDAO = new ChapterDAO();
        chapterDAO.setId(chapter.getId());
        chapterDAO.setChapterNo(chapter.getChapterNo());
        chapterDAO.setChapterTitle(chapter.getChapterTitle());
        chapterDAO.setContent(chapter.getContent());
        chapterDAO.setStoryId(chapter.getStoryId());
        return chapterDAO;
    }
}
