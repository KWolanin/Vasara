package com.kai.Vasara.controller.story;

import com.kai.Vasara.model.story.StoryInfo;
import com.kai.Vasara.service.story.FavoriteServiceStory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteStoryController {

    @Autowired
    public FavoriteStoryController(FavoriteServiceStory favouriteService) {
        this.favouriteService = favouriteService;
    }
   private final FavoriteServiceStory favouriteService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addFavorite(@RequestBody FavRequest fav) {
        return new ResponseEntity<>(favouriteService.add(fav.getUser(), fav.getStory()), HttpStatus.OK);
    }

    @PostMapping("/is")
    public ResponseEntity<Boolean> isFav(@RequestBody FavRequest fav) {
        return new ResponseEntity<>(favouriteService.is(fav.getUser(), fav.getStory()), HttpStatus.OK);
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<Page<StoryInfo>> myFavs(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "5") int size, @PathVariable long id) {
        return new ResponseEntity<>(favouriteService.get(page, size, id), HttpStatus.OK);
    }

    @GetMapping("/count/{id}")
    public ResponseEntity<Integer> countMine(@PathVariable Long id) {
        return new ResponseEntity<>(favouriteService.count(id), HttpStatus.OK);
    }

    @Data
    public static class FavRequest {
        private long user;
        private long story;
    }
}
