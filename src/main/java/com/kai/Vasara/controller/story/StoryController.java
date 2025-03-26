package com.kai.Vasara.controller.story;

import com.kai.Vasara.model.SearchCriteria;
import com.kai.Vasara.model.story.StoryDTO;
import com.kai.Vasara.model.story.StoryInfo;
import com.kai.Vasara.service.story.DeleteStoryService;
import com.kai.Vasara.service.story.EditStoryService;
import com.kai.Vasara.service.story.GetStoryAccordingToCriteria;
import com.kai.Vasara.service.story.GetStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/stories")
public class StoryController {

    private final GetStoryService getStoryService;
    private final EditStoryService editStoryService;
    private final DeleteStoryService deleteStoryService;
    private final GetStoryAccordingToCriteria getStoryAccordingToCriteriaService;

    @Autowired
    public StoryController(GetStoryService getStoryService, EditStoryService editStoryService, DeleteStoryService deleteStoryService, GetStoryAccordingToCriteria getStoryAccordingToCriteriaService) {
        this.getStoryService = getStoryService;
        this.editStoryService = editStoryService;
        this.deleteStoryService = deleteStoryService;
        this.getStoryAccordingToCriteriaService = getStoryAccordingToCriteriaService;
    }

    @PostMapping("/all")
    public ResponseEntity<Page<StoryInfo>> getStories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "updateDt") String sortBy,
            @RequestBody SearchCriteria searchCriteria) {
        return new ResponseEntity<>(getStoryAccordingToCriteriaService.getPage(page, size, searchCriteria, sortBy), HttpStatus.OK);
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<Page<StoryInfo>> getMyStories(@PathVariable Long id,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "5") int size) {
        return new ResponseEntity<>(getStoryService.getMyStories(id, page, size), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStory(@RequestBody StoryDTO storyDTO) {
        editStoryService.saveStory(storyDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editStory(@RequestBody StoryDTO storyDTO) {
        editStoryService.editStory(storyDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStory(@PathVariable Long id) {
        deleteStoryService.deleteStory(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(getStoryService.count(), HttpStatus.OK);
    }

    @GetMapping("/mines/count/{id}")
    public ResponseEntity<Long> countMine(@PathVariable Long id) {
        return new ResponseEntity<>(getStoryService.countMine(id), HttpStatus.OK);
    }
}
