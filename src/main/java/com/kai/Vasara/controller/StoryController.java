package com.kai.Vasara.controller;

import com.kai.Vasara.model.Criteria;
import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/stories")
public class StoryController {

    private final StoryService storyService;

    @Autowired
    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @PostMapping("/all")
    public ResponseEntity<Page<StoryDAO>> getStories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "updateDt") String sortBy,
            @RequestBody Criteria criteria) {
        return new ResponseEntity<>(storyService.getPage(page, size, criteria, sortBy), HttpStatus.OK);
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<Page<StoryDAO>> getMyStories(@PathVariable Long id,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "5") int size) {
        return new ResponseEntity<>(storyService.getMyStories(id, page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryDAO> getStory(@PathVariable Long id) {
        return new ResponseEntity<>(storyService.getStory(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStory(@RequestBody StoryDAO storyDAO) {
        storyService.saveStory(storyDAO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editStory(@RequestBody StoryDAO storyDAO) {
        storyService.editStory(storyDAO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(storyService.count(), HttpStatus.OK);
    }

    @GetMapping("/mines/count/{id}")
    public ResponseEntity<Long> countMine(@PathVariable Long id) {
        return new ResponseEntity<>(storyService.countMine(id), HttpStatus.OK);
    }
}
