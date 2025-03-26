package com.kai.Vasara.controller.story;

import com.kai.Vasara.model.story.StoryInfo;
import com.kai.Vasara.service.story.ReadLaterService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/reads")
@RestController
public class ReadLaterController {

    @Autowired
    public ReadLaterController(ReadLaterService readService) {
        this.readService = readService;
    }
    private final ReadLaterService readService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addReadLater(@RequestBody ReadLaterController.ReadRequest fav) {
        return new ResponseEntity<>(readService.add(fav.getUser(), fav.getStory()), HttpStatus.OK);
    }

    @PostMapping("/is")
    public ResponseEntity<Boolean> isReadLater(@RequestBody ReadLaterController.ReadRequest fav) {
        return new ResponseEntity<>(readService.is(fav.getUser(), fav.getStory()), HttpStatus.OK);
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<Page<StoryInfo>> myReads(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "5") int size, @PathVariable long id) {
        return new ResponseEntity<>(readService.get(page, size, id), HttpStatus.OK);
    }

    @GetMapping("/count/{id}")
    public ResponseEntity<Integer> countMine(@PathVariable Long id) {
        return new ResponseEntity<>(readService.count(id), HttpStatus.OK);
    }

    @GetMapping("/random/{id}")
    public ResponseEntity<Map<Long, String>> getRandom(@PathVariable Long id) {
        return new ResponseEntity<>(readService.getRandom(id), HttpStatus.OK);
    }

    @Data
    public static class ReadRequest {
        private long user;
        private long story;
    }
}
