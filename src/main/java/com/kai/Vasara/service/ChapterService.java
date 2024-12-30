package com.kai.Vasara.service;

import com.kai.Vasara.entity.Chapter;
import com.kai.Vasara.model.ChapterDAO;
import com.kai.Vasara.repository.ChapterRepository;
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
        Optional<Chapter> c = chapterRepository.findByStoryIdAndNumberNo(storyId, chapterNo);
        if (c.isPresent()) {
            return from(c.get());
        }
        return new ChapterDAO();
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

    @Transactional
    public Boolean checkIsNextOrPrevious(Long storyId, Long chapterNo) {
        Optional<Chapter> c = chapterRepository.findByStoryIdAndNumberNo(storyId, chapterNo);
        return c.isPresent();
    }

    public Boolean deleteChaptersForStory(Long storyId) {
        return chapterRepository.deleteAllByStoryId(storyId) > 0;
    }

    @Transactional
    public List<ChapterDAO> getChapters(Long storyId) {
        return chapterRepository.findChaptersDataByStoryId(storyId)
                .stream()
                .map(this::from)
                .toList();
    }

    @Transactional
    public Boolean editChaptersOrder(List<ChapterDAO> chapters) {
        try {
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
            return true;
        } catch (Exception e) {
            System.err.println("Error updating chapters order: " + e.getMessage());
            return false;
        }
    }

    public Boolean editChapterContent(ChapterDAO chapterDAO) {
        // todo
        return true;
    }


    public Boolean deleteChapter(Long id) {
        if (chapterRepository.existsById(id)) {
            chapterRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
