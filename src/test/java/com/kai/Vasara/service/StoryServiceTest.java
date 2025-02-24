package com.kai.Vasara.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.model.Criteria;
import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.repository.ChapterRepository;
import com.kai.Vasara.repository.StoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class StoryServiceTest {

    @InjectMocks
    StoryService storyService;

    @Mock
    StoryRepository storyRepository;

    @Mock
    ChapterRepository chapterRepository;

    @Mock ChapterService chapterService;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    FavoriteService favoriteService;

    @Mock
    FollowingService followingService;

    @Mock
    ReadService readService;


    @Test
    void updateStoryTagsAndFandoms_test() {
        Story story = new Story();
        StoryDAO storyDAO = new StoryDAO();
        storyDAO.setTags(Arrays.asList("A", "B", "C"));
        storyDAO.setFandoms(Arrays.asList("X", "Y", "Z"));

        storyService.updateStoryTagsAndFandoms(story, storyDAO);

        assertEquals("[\"A\",\"B\",\"C\"]", story.getTags());
        assertEquals("[\"X\",\"Y\",\"Z\"]", story.getFandoms());
    }

    @Test
    void updateStoryTagsAndFandoms_test_no_tags() {
        Story story = new Story();
        StoryDAO storyDAO = new StoryDAO();
        storyDAO.setTags(List.of(""));
        storyDAO.setFandoms(null);

        storyService.updateStoryTagsAndFandoms(story, storyDAO);

        assertEquals("[\"\"]", story.getTags());
        assertEquals("[\"\"]", story.getFandoms());
    }

    @Test
    void splitAndRemoveQuotes_test() {
        String input = "[\"A\",\"B\",\"C\"]";

        List<String> results = StoryService.splitAndRemoveQuotes(input);

        assertEquals(3, results.size());
        assertEquals("A", results.get(0));
        assertEquals("B", results.get(1));
        assertEquals("C", results.get(2));
    }

    @Test
    void splitAndRemoveQuotes_test_no_tag() {
        String input = "[]";
        List<String> results = StoryService.splitAndRemoveQuotes(input);
        assertTrue(results.isEmpty());

        input = "";
        results = StoryService.splitAndRemoveQuotes(input);
        assertTrue(results.isEmpty());

        input = null;
        results = StoryService.splitAndRemoveQuotes(input);
        assertTrue(results.isEmpty());
    }


    @Test
    void joinAndAddQuotes_test() {
        List<String> tags = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        String result = StoryService.joinAndAddQuotes(tags);
        assertEquals("[\"A\",\"B\",\"C\",\"D\"]", result);
    }

    @Test
    void joinAndAddQuotes_test_null_empty() {
        String result = StoryService.joinAndAddQuotes(null);
        assertEquals("[]", result);
        result = StoryService.joinAndAddQuotes(new ArrayList<>());
        assertEquals("[]", result);
    }

    @Test
    void getAll_returnsListOfStoryDAO() {
        Story story1 = new Story();
        story1.setId(1L);
        story1.setTitle("Story 1");
        story1.setAuthor(Author.builder().id(1L).username("username").build());


        Story story2 = new Story();
        story2.setId(2L);
        story2.setTitle("Story 2");
        story2.setAuthor(Author.builder().id(1L).username("username").build());


        when(storyRepository.findAll()).thenReturn(List.of(story1, story2));

        List<StoryDAO> result = storyService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Story 1", result.get(0).getTitle());
        assertEquals("Story 2", result.get(1).getTitle());
        verify(storyRepository).findAll();
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void getPage_withCriteria_returnsPageOfStoryDAO() {
        Criteria criteria = new Criteria();
        criteria.setTitle("Story");
        criteria.setAuthor("Author");

        Story story1 = new Story();
        story1.setId(1L);
        story1.setTitle("Story 1");
        story1.setAuthor(Author.builder().id(1L).username("username").build());


        Page<Story> page = new PageImpl<>(List.of(story1), PageRequest.of(0, 1), 1);
        when(storyRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<StoryDAO> result = storyService.getPage(1, 1, criteria, "title");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Story 1", result.getContent().get(0).getTitle());
        verify(storyRepository).findAll(any(Specification.class), any(Pageable.class));
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void getPage_withoutCriteria_returnsPageOfStoryDAO() {
        Criteria criteria = new Criteria();
        Story story1 = new Story();
        story1.setId(1L);
        story1.setTitle("Story 1");
        story1.setAuthor(Author.builder().id(1L).username("username").build());


        Page<Story> page = new PageImpl<>(List.of(story1), PageRequest.of(0, 1), 1);
        when(storyRepository.findAllWithChapters(any(Pageable.class))).thenReturn(page);

        Page<StoryDAO> result = storyService.getPage(1, 1, criteria, "title");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Story 1", result.getContent().get(0).getTitle());
        verify(storyRepository).findAllWithChapters(any(Pageable.class));
        verifyNoMoreInteractions(storyRepository);
    }


    @Test
    void getMyStories_returnsPageOfStoryDAO() {
        Story story1 = new Story();
        story1.setId(1L);
        story1.setTitle("Story 1");
        story1.setUpdateDt(ZonedDateTime.now());
        story1.setAuthor(Author.builder().id(1L).username("username").build());

        Sort sort = Sort.by(Sort.Order.desc("updateDt"));
        Page<Story> page = new PageImpl<>(List.of(story1), PageRequest.of(0, 1, sort), 1);
        //when(storyRepository.findAllByAuthorId(1L, PageRequest.of(0, 1, sort))).thenReturn(page);
        when(storyRepository.findAllByAuthorId(eq(1L), any(Pageable.class))).thenReturn(page);

        Page<StoryDAO> result = storyService.getMyStories(1L, 1, 1);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Story 1", result.getContent().get(0).getTitle());
        verify(storyRepository).findAllByAuthorId(1L, PageRequest.of(0, 1, sort));
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void getStory_storyExists_returnsStoryDAO() {
        Story story = new Story();
        story.setId(1L);
        story.setTitle("Story 1");
        story.setAuthor(Author.builder().id(1L).username("username").build());


        when(storyRepository.findById(1L)).thenReturn(Optional.of(story));

        StoryDAO result = storyService.getStory(1L);

        assertNotNull(result);
        assertEquals("Story 1", result.getTitle());
        verify(storyRepository).findById(1L);
        verifyNoMoreInteractions(storyRepository);
    }


    @Test
    void saveStory_validStory_returnsTrue() {
        StoryDAO storyDAO = new StoryDAO();
        storyDAO.setTitle("New Story");

        when(storyRepository.save(any(Story.class))).thenReturn(new Story());

    storyService.saveStory(storyDAO);

        verify(storyRepository).save(any(Story.class));
        verifyNoMoreInteractions(storyRepository);
    }


    @Test
    void deleteStory_storyExists_returnsTrue() {
        when(storyRepository.deleteStoryById(1L)).thenReturn(1);

        storyService.deleteStory(1L);

        verify(storyRepository).deleteStoryById(1L);
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void deleteStory_storyDoesNotExist_returnsFalse() {
        when(storyRepository.deleteStoryById(1L)).thenReturn(0);

    storyService.deleteStory(1L);

        verify(storyRepository).deleteStoryById(1L);
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void editStory_storyExists_returnsTrue() {
        StoryDAO storyDAO = new StoryDAO();
        storyDAO.setId(1L);
        storyDAO.setTitle("Updated Story");

        Story existingStory = new Story();
        existingStory.setId(1L);
        existingStory.setTitle("Old Story");

        when(storyRepository.findById(1L)).thenReturn(Optional.of(existingStory));
        when(storyRepository.save(existingStory)).thenReturn(existingStory);

        storyService.editStory(storyDAO);

        assertEquals("Updated Story", existingStory.getTitle());
        verify(storyRepository).findById(1L);
        verify(storyRepository).save(existingStory);
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void editStory_storyDoesNotExist_returnsFalse() {
        StoryDAO storyDAO = new StoryDAO();
        storyDAO.setId(1L);
        storyDAO.setTitle("Updated Story");

        when(storyRepository.findById(1L)).thenReturn(Optional.empty());

        storyService.editStory(storyDAO);

        verify(storyRepository).findById(1L);
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void count_returnsTotalStoryCount() {
        when(storyRepository.count()).thenReturn(100L);

        long result = storyService.count();

        assertEquals(100L, result);
        verify(storyRepository).count();
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void countMine_returnsCountOfAuthorsStories() {
        when(storyRepository.countMine(1L)).thenReturn(5L);

        long result = storyService.countMine(1L);

        assertEquals(5L, result);
        verify(storyRepository).countMine(1L);
        verifyNoMoreInteractions(storyRepository);
    }



}