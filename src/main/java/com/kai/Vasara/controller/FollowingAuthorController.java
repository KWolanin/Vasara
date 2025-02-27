package com.kai.Vasara.controller;

import com.kai.Vasara.service.FollowingAuthorService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authorsfollows")
public class FollowingAuthorController {

    private final FollowingAuthorService service;

    public FollowingAuthorController(FollowingAuthorService service) {
        this.service = service;
    }


    @PostMapping("/add")
    public ResponseEntity<Boolean> addFollowingAuthor(@RequestBody FollowingAuthorController.FollowAuthorRequest foll) {
        return new ResponseEntity<>(service.add(foll.getFollowing(), foll.getFollowed()), HttpStatus.OK);
    }

    @PostMapping("/is")
    public ResponseEntity<Boolean> isFollowedAuthor(@RequestBody FollowingAuthorController.FollowAuthorRequest foll) {
        return new ResponseEntity<>(service.is(foll.getFollowing(), foll.getFollowed()), HttpStatus.OK);
    }

    @Data
    public static class FollowAuthorRequest {
        private long following;
        private long followed;
    }
}
