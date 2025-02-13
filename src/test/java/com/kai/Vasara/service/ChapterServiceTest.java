package com.kai.Vasara.service;

import com.kai.Vasara.entity.Chapter;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.model.ChapterDAO;
import com.kai.Vasara.repository.ChapterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class ChapterServiceTest {

    @Mock
    ChapterRepository chapterRepository;

    @InjectMocks
    ChapterService chapterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEditChaptersOrder_Success() {
        List<ChapterDAO> chapterDAOs = prepareDaos();

        List<Chapter> existingChapters = prepareChapters();

        when(chapterRepository.findAllById(anyList())).thenReturn(existingChapters);
        when(chapterRepository.saveAll(anyList())).thenReturn(existingChapters);

        boolean result = chapterService.editChaptersOrder(chapterDAOs);

        Assertions.assertTrue(result);
        verify(chapterRepository).saveAll(anyList());
    }

    @Test
    void testEditChaptersOrder_Failure() {
        List<ChapterDAO> chapterDAOs = prepareDaos();

        when(chapterRepository.findAllById(anyList())).thenThrow(new RuntimeException("Database error"));

        boolean result = chapterService.editChaptersOrder(chapterDAOs);

        assertFalse(result);
    }

    private List<ChapterDAO> prepareDaos() {
        ChapterDAO c1 = ChapterDAO.builder()
                .chapterNo(1)
                .chapterTitle("Chapter 1")
                .storyId(1)
                .id(1)
                .content("content1")
                .build();

        ChapterDAO c2 = ChapterDAO.builder()
                .chapterNo(2)
                .chapterTitle("Chapter 2")
                .storyId(1)
                .id(2)
                .content("content2")
                .build();

        ChapterDAO c3 = ChapterDAO.builder()
                .chapterNo(3)
                .chapterTitle("Chapter 3")
                .storyId(1)
                .id(3)
                .content("content3")
                .build();

        ChapterDAO c4 = ChapterDAO.builder()
                .chapterNo(4)
                .chapterTitle("Chapter 4")
                .storyId(1)
                .id(4)
                .content("content4")
                .build();

        return new ArrayList<>(Arrays.asList(c1, c2, c3, c4));
    }

    private List<Chapter> prepareChapters() {
        Story story = new Story();
        story.setId(1L);

        Chapter c1 = Chapter.builder()
                .chapterNo(1)
                .chapterTitle("Chapter 1")
                .story(story)
                .id(1L)
                .content("content1")
                .build();

        Chapter c2 = Chapter.builder()
                .chapterNo(2)
                .chapterTitle("Chapter 2")
                .story(story)
                .id(2L)
                .content("content2")
                .build();

        Chapter c3 = Chapter.builder()
                .chapterNo(3)
                .chapterTitle("Chapter 3")
                .story(story)
                .id(3L)
                .content("content3")
                .build();

        Chapter c4 = Chapter.builder()
                .chapterNo(4)
                .chapterTitle("Chapter 4")
                .story(story)
                .id(4L)
                .content("content4")
                .build();

        return new ArrayList<>(Arrays.asList(c1, c2, c3, c4));
    }


}