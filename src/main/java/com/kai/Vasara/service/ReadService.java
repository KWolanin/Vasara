package com.kai.Vasara.service;

import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.ReadStories;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.exception.AuthorError;
import com.kai.Vasara.exception.AuthorException;
import com.kai.Vasara.exception.StoryError;
import com.kai.Vasara.exception.StoryException;
import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.repository.AuthorRepository;
import com.kai.Vasara.repository.ReadRepository;
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

@Component
@Service
@Slf4j
public class ReadService implements ActionService<ReadStories> {

    private final AuthorRepository authorRepository;
    private final StoryRepository storyRepository;
    private final ReadRepository readRepository;
    private final AuthorService authorService;
    private final StoryService storyService;

    @Autowired
    public ReadService(AuthorRepository authorRepository, StoryRepository storyRepository, ReadRepository readRepository,
                            AuthorService authorService, StoryService storyService) {
        this.authorRepository = authorRepository;
        this.storyRepository = storyRepository;
        this.readRepository = readRepository;
        this.authorService = authorService;
        this.storyService = storyService;
    }

    @Override
    public boolean add(long authorId, long storyId) {
            Optional<ReadStories> follow = check(authorId, storyId);
            if (follow.isPresent()) {
                log.warn("Story is removed from read later");
                readRepository.delete(follow.get());
                return false;
            }
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new StoryException(StoryError.STORY_NOT_FOUND));
            ReadStories read = new ReadStories();
            read.setAuthor(author);
            read.setStory(story);
            read.setAddedAt(ZonedDateTime.now());
            readRepository.save(read);
            return true;
    }



    @Override
    public Optional<ReadStories> check(long authorId, long storyId) {
        return readRepository.findByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    public boolean is(long authorId, long storyId) {
        return readRepository.existsByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    public Page<StoryDAO> get(int page, int size, long id) {
        Pageable pageable = PageRequest.of(page -1, size);
        List<Story> readStories;
        List<StoryDAO> daos = new ArrayList<>();
        Optional<Author> author = authorService.find(id);
        if (author.isPresent()) {
            readStories = readRepository.findByAuthor(author.get(), pageable).stream().map(ReadStories::getStory).toList();
            readStories.forEach(story -> daos.add(storyService.from(story)));
            return  new PageImpl<>(daos, pageable, readStories.size());
        }
        return  new PageImpl<>(daos, pageable, 0);
    }

    @Override
    public void delete(long id) {
        readRepository.deleteByStoryId(id);
    }

    @Override
    public int count(long id) {
       return readRepository.countByAuthorId(id);
    }
}
