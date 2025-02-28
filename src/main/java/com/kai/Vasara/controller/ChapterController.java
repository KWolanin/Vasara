package com.kai.Vasara.controller;

import com.kai.Vasara.model.ChapterDAO;
import com.kai.Vasara.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    @Autowired
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping("/read/{storyId}/{chapterNo}")
    public ResponseEntity<ChapterDAO> getChapterByStoryAndNumber(@PathVariable Long storyId, @PathVariable Long chapterNo) {
        return new ResponseEntity<>(chapterService.getChapterByStoryIdAndNumber(storyId, chapterNo), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addChapter(@RequestBody ChapterDAO chapterDAO) {
        chapterService.saveChapter(chapterDAO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/order")
    public ResponseEntity<?> editChaptersOrder(@RequestBody List<ChapterDAO> chapters) {
        chapterService.editChaptersOrder(chapters);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/navigable/{storyId}/{chapterNo}")
    public ResponseEntity<Boolean> CheckIsByStoryAndNumber(@PathVariable Long storyId, @PathVariable Long chapterNo) {
        return new ResponseEntity<>(chapterService.checkIsNextOrPrevious(storyId, chapterNo), HttpStatus.OK);
    }

    @GetMapping("/all/{storyId}")
    public ResponseEntity<List<ChapterDAO>> getChaptersForStory(@PathVariable Long storyId) {
        return new ResponseEntity<>(chapterService.getChapters(storyId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chapterId}")
    public ResponseEntity<?> deleteChapter(@PathVariable Long chapterId) {
        chapterService.deleteChapter(chapterId);
        return ResponseEntity.ok().build();
    }
}
