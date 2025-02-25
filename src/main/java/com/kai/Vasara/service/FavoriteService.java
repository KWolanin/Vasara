package com.kai.Vasara.service;


import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.FavoriteStories;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.exception.AuthorError;
import com.kai.Vasara.exception.AuthorException;
import com.kai.Vasara.exception.StoryError;
import com.kai.Vasara.exception.StoryException;
import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.repository.AuthorRepository;
import com.kai.Vasara.repository.FavoriteRepository;
import com.kai.Vasara.repository.StoryRepository;
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
            Optional<FavoriteStories> favorite = check(authorId, storyId);
            if (favorite.isPresent()) {
                log.warn("Story is removed from favorites");
                favoriteRepository.delete(favorite.get());
                return false;
            }
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
            Story story = storyRepository.findById(storyId)
                    .orElseThrow(() -> new StoryException(StoryError.STORY_NOT_FOUND));
            FavoriteStories fav = new FavoriteStories();
            fav.setAuthor(author);
            fav.setStory(story);
            fav.setAddedAt(ZonedDateTime.now());
            favoriteRepository.save(fav);
            return true;
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
        List<Story> favStories;
        List<StoryDAO> daos = new ArrayList<>();
        Optional<Author> author = authorService.find(id);
        if (author.isPresent()) {
            favStories = favoriteRepository.findByAuthor(author.get(), pageable).stream().map(FavoriteStories::getStory).toList();
            favStories.forEach(story -> daos.add(storyService.from(story)));
            return  new PageImpl<>(daos, pageable, favStories.size());
        }
        return  new PageImpl<>(daos, pageable, 0);
        }

    @Override
    public void delete(long id) {
        favoriteRepository.deleteByStoryId(id);
    }

    @Override
    public int count(long id) {
        return favoriteRepository.countByAuthorId(id);
    }
}
