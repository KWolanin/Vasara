package com.kai.Vasara.service.story;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.story.FollowStory;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.exception.author.AuthorError;
import com.kai.Vasara.exception.author.AuthorException;
import com.kai.Vasara.exception.story.StoryError;
import com.kai.Vasara.exception.story.StoryException;
import com.kai.Vasara.model.story.StoryInfo;
import com.kai.Vasara.repository.author.AuthorRepository;
import com.kai.Vasara.repository.story.FollowStoryRepository;
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
public class FollowServiceStory implements StoryActionService<FollowStory> {

    private final AuthorRepository authorRepository;
    private final StoryRepository storyRepository;
    private final FollowStoryRepository followStoryRepository;
    private final AuthorService authorService;

    @Autowired
    public FollowServiceStory(AuthorRepository authorRepository, StoryRepository storyRepository, FollowStoryRepository followStoryRepository,
                              AuthorService authorService) {
        this.authorRepository = authorRepository;
        this.storyRepository = storyRepository;
        this.followStoryRepository = followStoryRepository;
        this.authorService = authorService;
    }

    @Override
    public boolean add(long authorId, long storyId) {
            Optional<FollowStory> follow = check(authorId, storyId);
            if (follow.isPresent()) {
                log.warn("Story is removed from follows");
                followStoryRepository.delete(follow.get());
                return false;
            }
            Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
            Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new StoryException(StoryError.STORY_NOT_FOUND));
            FollowStory fol = new FollowStory();
            fol.setAuthor(author);
            fol.setStory(story);
            fol.setAddedAt(ZonedDateTime.now());
            followStoryRepository.save(fol);
            return true;
    }


    @Override
    public Optional<FollowStory> check(long authorId, long storyId) {
        return followStoryRepository.findByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    public boolean is(long authorId, long storyId) {
        return followStoryRepository.existsByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoryInfo> get(int page, int size, long id) {
        Pageable pageable = PageRequest.of(page -1, size);
        List<Story> followStories;
        List<StoryInfo> daos = new ArrayList<>();
        Optional<Author> author = authorService.find(id);
        if (author.isPresent()) {
            followStories = followStoryRepository.findByAuthor(author.get(), pageable).stream().map(FollowStory::getStory).toList();
            daos.addAll(followStories.stream().map(StoryInfo::from).toList());
            return  new PageImpl<>(daos, pageable, followStories.size());
        }
        return new PageImpl<>(daos, pageable, 0);
    }

    @Override
    public void delete(long id) {
        followStoryRepository.deleteByStoryId(id);
    }

    @Override
    public int count(long id) {
        return followStoryRepository.countByAuthorId(id);
    }
}
