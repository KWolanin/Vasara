package com.kai.Vasara.service;

import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.FollowingStories;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.repository.AuthorRepository;
import com.kai.Vasara.repository.FollowingRepository;
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
public class FollowingService implements ActionService<FollowingStories> {

    private final AuthorRepository authorRepository;
    private final StoryRepository storyRepository;
    private final FollowingRepository followingRepository;
    private final AuthorService authorService;
    private final StoryService storyService;

    @Autowired
    public FollowingService(AuthorRepository authorRepository, StoryRepository storyRepository, FollowingRepository followingRepository,
                           AuthorService authorService, StoryService storyService) {
        this.authorRepository = authorRepository;
        this.storyRepository = storyRepository;
        this.followingRepository = followingRepository;
        this.authorService = authorService;
        this.storyService = storyService;
    }

    @Override
    public boolean add(long authorId, long storyId) {
        try {
            Optional<FollowingStories> follow = check(authorId, storyId);
            if (follow.isPresent()) {
                log.warn("Story is removed from follows");
                followingRepository.delete(follow.get());
                return false;
            }
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found"));
            Story story = storyRepository.findById(storyId)
                    .orElseThrow(() -> new EntityNotFoundException("Story not found"));
            FollowingStories fol = new FollowingStories();
            fol.setAuthor(author);
            fol.setStory(story);
            fol.setAddedAt(ZonedDateTime.now());
            followingRepository.save(fol);
            return true;
    } catch (EntityNotFoundException e) {
        log.error("Entity not found: {}", e.getMessage());
        throw e;
    } catch (Exception e) {
        log.error("Unexpected error while adding/removing following: {}", e.getMessage());
        throw new RuntimeException("Error occurred while processing the following action");
    }
    }


    @Override
    public Optional<FollowingStories> check(long authorId, long storyId) {
        return followingRepository.findByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    public boolean is(long authorId, long storyId) {
        return followingRepository.existsByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    public Page<StoryDAO> get(int page, int size, long id) {
        Pageable pageable = PageRequest.of(page -1, size);
        List<Story> followStories;
        List<StoryDAO> daos = new ArrayList<>();
        Optional<Author> author = authorService.find(id);
        if (author.isPresent()) {
            followStories = followingRepository.findByAuthor(author.get(), pageable).stream().map(FollowingStories::getStory).toList();
            followStories.forEach(story -> daos.add(storyService.from(story)));
            return  new PageImpl<>(daos, pageable, followStories.size());
        }
        return new PageImpl<>(daos, pageable, 0);

    }

    @Override
    public void delete(long id) {
        followingRepository.deleteByStoryId(id);
    }

    @Override
    public int count(long id) {
        return followingRepository.countByAuthorId(id);
    }
}
