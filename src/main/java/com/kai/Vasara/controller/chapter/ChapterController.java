package com.kai.Vasara.controller.chapter;

import com.kai.Vasara.model.chapter.ChapterDTO;
import com.kai.Vasara.model.chapter.ChapterInfo;
import com.kai.Vasara.model.chapter.ChapterWithParagraphsDTO;
import com.kai.Vasara.service.chapter.GetWholeChapterService;
import com.kai.Vasara.service.chapter.GetSplitChapterService;
import com.kai.Vasara.service.chapter.EditChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    private final GetWholeChapterService getWholeChapterService;
    private final GetSplitChapterService paragraphsService;
    private final EditChapterService editChapterService;

    @Autowired
    public ChapterController(GetWholeChapterService getWholeChapterService, GetSplitChapterService paragraphsService, EditChapterService editChapterService) {
        this.getWholeChapterService = getWholeChapterService;
        this.paragraphsService = paragraphsService;
        this.editChapterService = editChapterService;
    }

    @GetMapping("/read/{storyId}/{chapterNo}")
    public ResponseEntity<ChapterDTO> getWholeChapter(@PathVariable Long storyId, @PathVariable Long chapterNo) {
        return new ResponseEntity<>(getWholeChapterService.getWholeChapter(storyId, chapterNo), HttpStatus.OK);
    }

    @GetMapping("/read/paragraphs/{storyId}/{chapterNo}")
    public ResponseEntity<ChapterWithParagraphsDTO> getSplitChapter(
            @PathVariable Long storyId,
            @PathVariable Long chapterNo,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "15") int limit
    ) {
        return ResponseEntity.ok(paragraphsService.getSplitChapter(storyId, chapterNo, offset, limit));
    }


    @PostMapping("/add")
    public ResponseEntity<?> addChapter(@RequestBody ChapterDTO chapterDTO) {
        editChapterService.saveChapter(chapterDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editChapter(@RequestBody ChapterDTO chapterDTO) {
        editChapterService.editChapter(chapterDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/order")
    public ResponseEntity<?> editChaptersOrder(@RequestBody List<ChapterInfo> chapters) {
        editChapterService.editChaptersOrder(chapters);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all/{storyId}")
    public ResponseEntity<List<ChapterInfo>> getChaptersInfoForStory(@PathVariable Long storyId) {
        return new ResponseEntity<>(getWholeChapterService.getChaptersInfo(storyId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chapterId}")
    public ResponseEntity<?> deleteChapter(@PathVariable Long chapterId) {
        editChapterService.deleteChapter(chapterId);
        return ResponseEntity.ok().build();
    }
}
