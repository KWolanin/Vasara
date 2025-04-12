package com.kai.Vasara.service.chapter;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.model.chapter.ChapterWithParagraphsDTO;
import com.kai.Vasara.model.chapter.ParagraphDTO;
import com.kai.Vasara.repository.chapter.ChapterRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSplitChapterServiceTest {

        @Mock
        private CacheService service; // zależność, która zwraca Chapter z bazy

        @InjectMocks
        private GetSplitChapterService getSplitChapterService; // testowana klasa

        @Test
        void getSplitChapter_returnsPaginatedParagraphsDTO() {
            Long storyId = 1L;
            Long chapterNo = 1L;
            int offset = 0;
            int limit = 2;

            Story story = new Story();
            story.setId(storyId);
            story.setChapters(List.of(new Chapter(), new Chapter(), new Chapter())); // 3 dummy chaptery – potrzebne dla isNext

            Author author = new Author();
            author.setUsername("testAuthor");
            author.setId(1L);

            story.setAuthor(author);

            Chapter chapter = new Chapter();
            chapter.setId(100L);
            chapter.setChapterNo(1);
            chapter.setChapterTitle("Chapter One");
            String contentHtml = "<p>Paragraph 1</p><p>Paragraph 2</p><p>Paragraph 3</p>"; // 3 akapity
            chapter.setContent(contentHtml);

            chapter.setPublished(ZonedDateTime.now());
            chapter.setUpdated(ZonedDateTime.now());
            chapter.setStory(story);

            when(service.getSplitChapter(storyId, chapterNo)).thenReturn(chapter);

            // when
            ChapterWithParagraphsDTO result = getSplitChapterService.getSplitChapter(storyId, chapterNo, offset, limit);

            assertNotNull(result);
            assertEquals(100L, result.getId());
            assertEquals(1, result.getChapterNo());
            assertEquals("Chapter One", result.getChapterTitle());
            assertEquals(2, result.getParagraphs().size());

            assertEquals("<p>Paragraph 1</p>", result.getParagraphs().get(0).getContent());
            assertEquals("<p>Paragraph 2</p>", result.getParagraphs().get(1).getContent());

            assertTrue(result.isNext());
            assertFalse(result.isPrevious());
        }
    }
