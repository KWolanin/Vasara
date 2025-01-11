package com.kai.Vasara.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.model.StoryDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StoryServiceTest {

    @InjectMocks
    StoryService storyService;

    @Mock
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
}