package com.kai.Vasara.service;


import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.model.chapter.ChapterDTO;
import com.kai.Vasara.repository.chapter.ChapterRepository;

import com.kai.Vasara.service.chapter.EditChapterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GetWholeChapterServiceTest {

    @Mock
    ChapterRepository chapterRepository;
    @InjectMocks
    EditChapterService editChapterService;

    @Test
    void editChaptersOrder() {
        ChapterDTO shouldBeFirst = ChapterDTO.builder()
                .id(2)
                .chapterNo(1)
                .chapterTitle("Published as 2nd, should be 1st").build();
        ChapterDTO shouldBeSecond = ChapterDTO.builder()
                .id(1)
                .chapterNo(2)
                .chapterTitle("Published as 1st, should be 2nd").build();


        Chapter isFirst = Chapter.builder()
                .id(1L)
                .chapterNo(2)
                .chapterTitle("Published as 2nd, should be 1st").build();
        Chapter isSecond = Chapter.builder()
                .id(2L)
                .chapterNo(1)
                .chapterTitle("Published as 1st, should be 2nd").build();

        when(chapterRepository.findAllById(anyList())).thenReturn(Arrays.asList(isFirst, isSecond));

        editChapterService.editChaptersOrder(Arrays.asList(shouldBeFirst, shouldBeSecond));

        assertEquals(1, shouldBeFirst.getChapterNo());
        assertEquals(2, shouldBeSecond.getChapterNo());
        assertEquals("Published as 2nd, should be 1st", shouldBeFirst.getChapterTitle());
        assertEquals("Published as 1st, should be 2nd", shouldBeSecond.getChapterTitle());

    }
}
