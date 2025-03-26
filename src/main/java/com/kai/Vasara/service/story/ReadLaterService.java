package com.kai.Vasara.service.story;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.story.ReadLaterStory;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.exception.author.AuthorError;
import com.kai.Vasara.exception.author.AuthorException;
import com.kai.Vasara.exception.story.StoryError;
import com.kai.Vasara.exception.story.StoryException;
import com.kai.Vasara.model.story.StoryInfo;
import com.kai.Vasara.repository.author.AuthorRepository;
import com.kai.Vasara.repository.story.ReadLaterRepository;
import com.kai.Vasara.repository.story.StoryRepository;
import com.kai.Vasara.service.EnvironmentService;
import com.kai.Vasara.service.author.AuthorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
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
import java.util.*;

@Component
@Service
@Slf4j
public class ReadLaterService implements StoryActionService<ReadLaterStory> {

    private final AuthorRepository authorRepository;
    private final StoryRepository storyRepository;
    private final ReadLaterRepository readLaterRepository;
    private final AuthorService authorService;
    private final EntityManager entityManager;
    private final EnvironmentService environmentService;

    @Autowired
    public ReadLaterService(AuthorRepository authorRepository, StoryRepository storyRepository, ReadLaterRepository readLaterRepository,
                            AuthorService authorService, EntityManager entityManager, EnvironmentService environmentService) {
        this.authorRepository = authorRepository;
        this.storyRepository = storyRepository;
        this.readLaterRepository = readLaterRepository;
        this.authorService = authorService;
        this.entityManager = entityManager;
        this.environmentService = environmentService;
    }

    @Override
    public boolean add(long authorId, long storyId) {
            Optional<ReadLaterStory> follow = check(authorId, storyId);
            if (follow.isPresent()) {
                log.warn("Story is removed from read later");
                readLaterRepository.delete(follow.get());
                return false;
            }
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new StoryException(StoryError.STORY_NOT_FOUND));
            ReadLaterStory read = new ReadLaterStory();
            read.setAuthor(author);
            read.setStory(story);
            read.setAddedAt(ZonedDateTime.now());
            readLaterRepository.save(read);
            return true;
    }



    @Override
    public Optional<ReadLaterStory> check(long authorId, long storyId) {
        return readLaterRepository.findByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    public boolean is(long authorId, long storyId) {
        return readLaterRepository.existsByAuthorIdAndStoryId(authorId, storyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoryInfo> get(int page, int size, long id) {
        Pageable pageable = PageRequest.of(page -1, size);
        List<Story> readStories;
        List<StoryInfo> daos = new ArrayList<>();
        Optional<Author> author = authorService.find(id);
        if (author.isPresent()) {
            readStories = readLaterRepository.findByAuthor(author.get(), pageable).stream().map(ReadLaterStory::getStory).toList();
            daos.addAll(readStories.stream().map(StoryInfo::from).toList());
            return  new PageImpl<>(daos, pageable, readStories.size());
        }
        return  new PageImpl<>(daos, pageable, 0);
    }

    @Override
    public void delete(long id) {
        readLaterRepository.deleteByStoryId(id);
    }

    @Override
    public int count(long id) {
       return readLaterRepository.countByAuthorId(id);
    }

    public Map<Long, String> getRandom(Long id) {
        String nativeQuery = "SELECT rs.story_id, s.title " +
                "FROM read_story rs " +
                "JOIN story s ON rs.story_id = s.id " +
                "WHERE rs.author_id = :authorId " +
                "ORDER BY " + (environmentService.isPostgreSQL() ? "RANDOM()" : "RAND()") + " LIMIT 1";

        Query query = entityManager.createNativeQuery(nativeQuery);
        query.setParameter("authorId", id);

        Object[] result;
        try {
            result = (Object[]) query.getSingleResult();
        } catch (NoResultException e) {
            throw new StoryException(StoryError.STORY_NOT_FOUND);
        }
        Long storyId = ((Number) result[0]).longValue();
        String title = (String) result[1];
        return Map.of(storyId, title);
    }
}
