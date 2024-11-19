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

@Service
@Component
public class ChapterService {

    private final ChapterRepository chapterRepository;
   // private final StoryService storyService;
   // private final AuthorService authorService;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
        //this.storyService = storyService;
        //this.authorService = authorService;
    }

    public List<Chapter> getAll() {
        return chapterRepository.findAll();
    }

    public Chapter getChapter(Long id) {
         Optional<Chapter> opt = chapterRepository.findById(id);
        return opt.orElse(null);
    }

    public Boolean saveChapter(Chapter chapter) {
        try {
            chapterRepository.save(chapter);
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
        Chapter c = chapterRepository.findByStoryIdAndNumberNo(storyId, chapterNo);
        return from(c);
    }

    private ChapterDAO from(Chapter chapter) {
        ChapterDAO c = new ChapterDAO();
        c.setId(chapter.getId());
        c.setChapterNo(chapter.getChapterNo());
        c.setChapterTitle(chapter.getChapterTitle());
        c.setContent(chapter.getContent());

       // c.setAuthor(authorService.getAuthor(chapter.getAuthorId()));
        //c.setStory(storyService.getStory(chapter.getStoryId()));
        c.setStoryId(chapter.getStoryId());

        return c;
    }
}
