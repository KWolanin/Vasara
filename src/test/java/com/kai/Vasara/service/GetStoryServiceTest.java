package com.kai.Vasara.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.mapper.Mapper;
import com.kai.Vasara.model.SearchCriteria;
import com.kai.Vasara.model.story.StoryDTO;
import com.kai.Vasara.model.story.StoryInfo;
import com.kai.Vasara.repository.chapter.ChapterRepository;
import com.kai.Vasara.repository.story.StoryRepository;
import com.kai.Vasara.service.author.AuthorService;
import com.kai.Vasara.service.chapter.GetWholeChapterService;
import com.kai.Vasara.service.story.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class GetStoryServiceTest {

    @Mock
    GetStoryService getStoryService;
    @Mock
    GetStoryAccordingToCriteria getStoryAccordingToCriteria;
    @Mock
    StoryRepository storyRepository;
    @Mock
    ChapterRepository chapterRepository;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    FavoriteServiceStory favoriteService;
    @Mock
    FollowServiceStory followingService;
    @Mock
    ReadLaterService readService;
    @Mock
    private EditStoryService editStoryService;
    @Mock
    private GetWholeChapterService getWholeChapterService;
    @Mock
    private AuthorService authorService;
    @Mock
    private Mapper mapper;

    @BeforeEach
    void setUp() {
        editStoryService = new EditStoryService(storyRepository, mapper);
        getStoryService = new GetStoryService(storyRepository, mapper);
        getStoryAccordingToCriteria = new GetStoryAccordingToCriteria(getStoryService);
    }

    @Test
    void splitAndRemoveQuotes_test() {
        String input = "[\"A\",\"B\",\"C\"]";
        List<String> results = mapper.splitAndRemoveQuotes(input);
        assertEquals(3, results.size());
        assertEquals("A", results.get(0));
        assertEquals("B", results.get(1));
        assertEquals("C", results.get(2));
    }

    @Test
    void splitAndRemoveQuotes_test_no_tag() {
        String input = "[]";
        List<String> results = mapper.splitAndRemoveQuotes(input);
        assertTrue(results.isEmpty());

        input = "";
        results = mapper.splitAndRemoveQuotes(input);
        assertTrue(results.isEmpty());

        input = null;
        results = mapper.splitAndRemoveQuotes(input);
        assertTrue(results.isEmpty());
    }


    @Test
    void joinAndAddQuotes_test() {
        List<String> tags = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        String result = mapper.joinAndAddQuotes(tags);
        assertEquals("[\"A\",\"B\",\"C\",\"D\"]", result);
    }

    @Test
    void joinAndAddQuotes_test_null_empty() {
        String result = mapper.joinAndAddQuotes(null);
        assertEquals("[]", result);
        result = mapper.joinAndAddQuotes(new ArrayList<>());
        assertEquals("[]", result);
    }

    @Test
    void getPage_withCriteria_returnsPageOfStoryDAO() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setTitle("Story");
        searchCriteria.setAuthor("Author");

        Story story1 = new Story();
        story1.setId(1L);
        story1.setTitle("Story 1");
        story1.setAuthor(Author.builder().id(1L).username("username").build());


        Page<Story> page = new PageImpl<>(List.of(story1), PageRequest.of(0, 1), 1);
        when(storyRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<StoryInfo> result = getStoryAccordingToCriteria.getPage(1, 1, searchCriteria, "title");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Story 1", result.getContent().get(0).getTitle());
        verify(storyRepository).findAll(any(Specification.class), any(Pageable.class));
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void getPage_withoutCriteria_returnsPageOfStoryDAO() {
        SearchCriteria searchCriteria = new SearchCriteria();
        Story story1 = new Story();
        story1.setId(1L);
        story1.setTitle("Story 1");
        story1.setAuthor(Author.builder().id(1L).username("username").build());


        Page<Story> page = new PageImpl<>(List.of(story1), PageRequest.of(0, 1), 1);
        when(storyRepository.findAllWithChapters(any(Pageable.class))).thenReturn(page);

        Page<StoryInfo> result = getStoryAccordingToCriteria.getPage(1, 1, searchCriteria, "title");

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
        when(storyRepository.findAllByAuthorId(eq(1L), any(Pageable.class))).thenReturn(page);

        Page<StoryInfo> result = getStoryService.getMyStories(1L, 1, 1);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Story 1", result.getContent().get(0).getTitle());
        verify(storyRepository).findAllByAuthorId(1L, PageRequest.of(0, 1, sort));
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void saveStory_validStory_returnsTrue() {
        StoryDTO storyDTO = new StoryDTO();
        storyDTO.setTitle("New Story");

        when(storyRepository.save(any(Story.class))).thenReturn(new Story());

        editStoryService.saveStory(storyDTO);

        verify(storyRepository).save(any(Story.class));
        verifyNoMoreInteractions(storyRepository);
    }


    @Test
    void editStory_storyExists_returnsTrue() {
        StoryDTO storyDTO = new StoryDTO();
        storyDTO.setId(1L);
        storyDTO.setTitle("Updated Story");

        Story existingStory = new Story();
        existingStory.setId(1L);
        existingStory.setTitle("Old Story");

        when(storyRepository.findById(1L)).thenReturn(Optional.of(existingStory));
        when(storyRepository.save(existingStory)).thenReturn(existingStory);

        editStoryService.editStory(storyDTO);

        assertEquals("Updated Story", existingStory.getTitle());
        verify(storyRepository).findById(1L);
        verify(storyRepository).save(existingStory);
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void editStory_storyDoesNotExist_returnsFalse() {
        StoryDTO storyDTO = new StoryDTO();
        storyDTO.setId(1L);
        storyDTO.setTitle("Updated Story");

        when(storyRepository.findById(1L)).thenReturn(Optional.empty());

        editStoryService.editStory(storyDTO);

        verify(storyRepository).findById(1L);
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void count_returnsTotalStoryCount() {
        when(storyRepository.count()).thenReturn(100L);

        long result = getStoryService.count();

        assertEquals(100L, result);
        verify(storyRepository).count();
        verifyNoMoreInteractions(storyRepository);
    }

    @Test
    void countMine_returnsCountOfAuthorsStories() {
        when(storyRepository.countMine(1L)).thenReturn(5L);

        long result = getStoryService.countMine(1L);

        assertEquals(5L, result);
        verify(storyRepository).countMine(1L);
        verifyNoMoreInteractions(storyRepository);
    }


}