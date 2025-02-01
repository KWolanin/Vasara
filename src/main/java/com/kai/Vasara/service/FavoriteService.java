package com.kai.Vasara.service;


import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.FavoriteStories;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.repository.AuthorRepository;
import com.kai.Vasara.repository.FavoriteRepository;
import com.kai.Vasara.repository.StoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Component
public class FavoriteService implements ActionService<FavoriteStories> {

    private final AuthorRepository authorRepository;
    private final StoryRepository storyRepository;
    private final FavoriteRepository favoriteRepository;
    private final AuthorService authorService;
    private final StoryService storyService;

    @Autowired
    public FavoriteService(AuthorRepository authorRepository, StoryRepository storyRepository, FavoriteRepository favoriteRepository,
                            AuthorService authorService, StoryService storyService) {
        this.authorRepository = authorRepository;
        this.storyRepository = storyRepository;
        this.favoriteRepository = favoriteRepository;
        this.authorService = authorService;
        this.storyService = storyService;
    }

    @Override
    public boolean add(long authorId, long storyId) {
        try {
            Optional<FavoriteStories> favorite = check(authorId, storyId);
            if (favorite.isPresent()) {
                log.warn("Story is removed from favorites");
                favoriteRepository.delete(favorite.get());
                return false;
            }
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found"));
            Story story = storyRepository.findById(storyId)
                    .orElseThrow(() -> new EntityNotFoundException("Story not found"));
            FavoriteStories fav = new FavoriteStories();
            fav.setAuthor(author);
            fav.setStory(story);
            fav.setAddedAt(ZonedDateTime.now());
            favoriteRepository.save(fav);
            return true;
        } catch (Exception e) {
            log.error("Error while adding/removing favorite: {}", e.getMessage());
            throw new RuntimeException("Error occurred while processing the favorite action");
        }
    }

    @Override
    public Optional<FavoriteStories> check(long authorId, long storyId) {
        return favoriteRepository.findByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    public boolean is(long authorId, long storyId) {
        return favoriteRepository.existsByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    public Page<StoryDAO> get(int page, int size, long id) {
        Pageable pageable = PageRequest.of(page -1, size);
        List<Story> favStories = null;
        Optional<Author> author = authorService.find(id);
            favStories = favoriteRepository.findByAuthor(author.get(), pageable).stream().map(FavoriteStories::getStory).toList();
            List<StoryDAO> daos = new ArrayList<>();
            favStories.forEach(story -> daos.add(storyService.from(story)));
            return  new PageImpl<>(daos, pageable, favStories.size());
        }
}
