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

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDAO> getChapter(@PathVariable Long id) {
        return new ResponseEntity<>(chapterService.getChapter(id), HttpStatus.OK);
    }

    @GetMapping("/read/{storyId}/{chapterNo}")
    public ResponseEntity<ChapterDAO> getChapterByStoryAndNumber(@PathVariable Long storyId, @PathVariable Long chapterNo) {
        return new ResponseEntity<>(chapterService.getChapterByStoryIdAndNumber(storyId, chapterNo), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addChapter(@RequestBody ChapterDAO chapterDAO) {
        return new ResponseEntity<>(chapterService.saveChapter(chapterDAO), HttpStatus.OK);
    }

    @PatchMapping("/editOrder")
    public ResponseEntity<Boolean> editChaptersOrder(@RequestBody List<ChapterDAO> chapters) {
        return new ResponseEntity<>(chapterService.editChaptersOrder(chapters), HttpStatus.OK);
    }

    @GetMapping("/isNextOrPrevious/{storyId}/{chapterNo}")
    public ResponseEntity<Boolean> CheckIsByStoryAndNumber(@PathVariable Long storyId, @PathVariable Long chapterNo) {
        return new ResponseEntity<>(chapterService.checkIsNextOrPrevious(storyId, chapterNo), HttpStatus.OK);
    }

    @GetMapping("/all/{storyId}")
    public ResponseEntity<List<ChapterDAO>> getChapters(@PathVariable Long storyId) {
        return new ResponseEntity<>(chapterService.getChapters(storyId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chapterId}")
    public ResponseEntity<Boolean> deleteChapter(@PathVariable Long chapterId) {
        return new ResponseEntity<>(chapterService.deleteChapter(chapterId), HttpStatus.OK);
    }
}
