package com.kai.Vasara.controller;

import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.service.FollowingServiceStory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/follows")
@RestController
public class FollowingController {

    @Autowired
    public FollowingController(FollowingServiceStory followingService) {
        this.followingService = followingService;
    }
    private final FollowingServiceStory followingService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addFollowing(@RequestBody FollowingController.FollowRequest fav) {
        return new ResponseEntity<>(followingService.add(fav.getUser(), fav.getStory()), HttpStatus.OK);
    }

    @PostMapping("/is")
    public ResponseEntity<Boolean> isFollowed(@RequestBody FollowingController.FollowRequest fav) {
        return new ResponseEntity<>(followingService.is(fav.getUser(), fav.getStory()), HttpStatus.OK);
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<Page<StoryDAO>> myFollows(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "5") int size, @PathVariable long id) {
        return new ResponseEntity<>(followingService.get(page, size, id), HttpStatus.OK);
    }

    @GetMapping("/count/{id}")
    public ResponseEntity<Integer> countMine(@PathVariable Long id) {
        return new ResponseEntity<>(followingService.count(id), HttpStatus.OK);
    }

    @Data
    public static class FollowRequest {
        private long user;
        private long story;
    }
}
