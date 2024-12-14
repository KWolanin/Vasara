package com.kai.Vasara.controller;

import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/stories")
public class StoryController {

    private final StoryService storyService;

    @Autowired
    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<StoryDAO>> getStories() {
        return new ResponseEntity<>(storyService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryDAO> getStory(@PathVariable Long id) {
        return new ResponseEntity<>(storyService.getStory(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addStory(@RequestBody StoryDAO storyDAO) {
        return new ResponseEntity<>(storyService.saveStory(storyDAO), HttpStatus.OK);
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<List<StoryDAO>> getMyStories(@PathVariable Long id) {
        return new ResponseEntity<>(storyService.getMyStories(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteStory(@PathVariable Long id) {
        return new ResponseEntity<>(storyService.deleteStory(id), HttpStatus.OK);
    }
}
