package com.kai.Vasara.service.story;


import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.story.FavoriteStory;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.exception.author.AuthorError;
import com.kai.Vasara.exception.author.AuthorException;
import com.kai.Vasara.exception.story.StoryError;
import com.kai.Vasara.exception.story.StoryException;
import com.kai.Vasara.model.story.StoryInfo;
import com.kai.Vasara.repository.author.AuthorRepository;
import com.kai.Vasara.repository.story.FavoriteStoryRepository;
import com.kai.Vasara.repository.story.StoryRepository;
import com.kai.Vasara.service.author.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Component
public class FavoriteServiceStory implements StoryActionService<FavoriteStory> {

    private final AuthorRepository authorRepository;
    private final StoryRepository storyRepository;
    private final FavoriteStoryRepository favoriteStoryRepository;
    private final AuthorService authorService;

    @Autowired
    public FavoriteServiceStory(AuthorRepository authorRepository, StoryRepository storyRepository, FavoriteStoryRepository favoriteStoryRepository,
                                AuthorService authorService) {
        this.authorRepository = authorRepository;
        this.storyRepository = storyRepository;
        this.favoriteStoryRepository = favoriteStoryRepository;
        this.authorService = authorService;
    }

    @Override
    public boolean add(long authorId, long storyId) {
            Optional<FavoriteStory> favorite = check(authorId, storyId);
            if (favorite.isPresent()) {
                log.warn("Story is removed from favorites");
                favoriteStoryRepository.delete(favorite.get());
                return false;
            }
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
            Story story = storyRepository.findById(storyId)
                    .orElseThrow(() -> new StoryException(StoryError.STORY_NOT_FOUND));
            FavoriteStory fav = new FavoriteStory();
            fav.setAuthor(author);
            fav.setStory(story);
            fav.setAddedAt(ZonedDateTime.now());
            favoriteStoryRepository.save(fav);
            return true;
    }

    @Override
    public Optional<FavoriteStory> check(long authorId, long storyId) {
        return favoriteStoryRepository.findByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    public boolean is(long authorId, long storyId) {
        return favoriteStoryRepository.existsByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoryInfo> get(int page, int size, long id) {
        Pageable pageable = PageRequest.of(page -1, size);
         List<StoryInfo> daos = new ArrayList<>();
        Optional<Author> author = authorService.find(id);
        if (author.isPresent()) {
            Page<FavoriteStory> favoriteStories = favoriteStoryRepository.findByAuthor(author.get(), pageable);
           List<Story> favStories = favoriteStories.stream().map(FavoriteStory::getStory).toList();
           daos.addAll(favStories.stream().map(StoryInfo::from).toList());
           return new PageImpl<>(daos, pageable, favStories.size());
        }
        return  new PageImpl<>(daos, pageable, 0);
        }

    @Override
    public void delete(long id) {
        favoriteStoryRepository.deleteByStoryId(id);
    }

    @Override
    public int count(long id) {
        return favoriteStoryRepository.countByAuthorId(id);
    }
}
