package com.kai.Vasara.controller.author;

import com.kai.Vasara.service.author.FollowAuthorService;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authorsfollows")
public class FollowAuthorController {

    private final FollowAuthorService service;

    public FollowAuthorController(FollowAuthorService service) {
        this.service = service;
    }


    @PostMapping("/add")
    public ResponseEntity<Boolean> addFollowingAuthor(@RequestBody FollowAuthorController.FollowAuthorRequest foll) {
        return new ResponseEntity<>(service.add(foll.getFollowing(), foll.getFollowed()), HttpStatus.OK);
    }

    @PostMapping("/is")
    public ResponseEntity<Boolean> isFollowedAuthor(@RequestBody FollowAuthorController.FollowAuthorRequest foll) {
        return new ResponseEntity<>(service.is(foll.getFollowing(), foll.getFollowed()), HttpStatus.OK);
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<Page<FollowAuthorService.AuthorInfo>> myFollowedAuthors(@RequestParam(defaultValue = "1") int page,
                                                                                  @RequestParam(defaultValue = "5") int size, @PathVariable long id) {
        return new ResponseEntity<>(service.get(page, size, id), HttpStatus.OK);
    }

    @GetMapping("/count/{id}")
    public ResponseEntity<Integer> countMine(@PathVariable Long id) {
        return new ResponseEntity<>(service.count(id), HttpStatus.OK);
    }

    @Data
    public static class FollowAuthorRequest {
        private long following;
        private long followed;
    }
}
