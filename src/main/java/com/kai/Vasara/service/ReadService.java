package com.kai.Vasara.service;

import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.ReadStories;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.repository.AuthorRepository;
import com.kai.Vasara.repository.ReadRepository;
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
        try {
            Optional<ReadStories> follow = check(authorId, storyId);
            if (follow.isPresent()) {
                log.warn("Story is removed from read later");
                readRepository.delete(follow.get());
                return false;
            }
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found"));
            Story story = storyRepository.findById(storyId)
                    .orElseThrow(() -> new EntityNotFoundException("Story not found"));
            ReadStories read = new ReadStories();
            read.setAuthor(author);
            read.setStory(story);
            read.setAddedAt(ZonedDateTime.now());
            readRepository.save(read);
            return true;
        } catch (Exception e) {
            log.error("Error while adding/removing read later: {}", e.getMessage());
            throw new RuntimeException("Error occurred while processing the read later action");
        }
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
        List<Story> readStories = null;
        Optional<Author> author = authorService.find(id);
        readStories = readRepository.findByAuthor(author.get(), pageable).stream().map(ReadStories::getStory).toList();
        List<StoryDAO> daos = new ArrayList<>();
        readStories.forEach(story -> daos.add(storyService.from(story)));
        return  new PageImpl<>(daos, pageable, readStories.size());
    }
}
