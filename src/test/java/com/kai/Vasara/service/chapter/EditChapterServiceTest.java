package com.kai.Vasara.service.chapter;

import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.mapper.Mapper;
import com.kai.Vasara.model.chapter.ChapterDTO;
import com.kai.Vasara.model.chapter.ChapterInfo;
import com.kai.Vasara.repository.chapter.ChapterRepository;
import com.kai.Vasara.repository.story.StoryRepository;
import com.kai.Vasara.service.EmailService;
import com.kai.Vasara.service.comment.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditChapterServiceTest {

   @Mock
   Mapper mapper;

   @Mock
   ChapterRepository chapterRepository;

   @Mock
    StoryRepository storyRepository;

   @Mock
    EmailService emailService;

   @Mock
    CommentService commentService;

   @InjectMocks
    EditChapterService editChapterService;

   @BeforeEach
   void setUp() {
       mapper = mock(Mapper.class);
       MockitoAnnotations.openMocks(this);
   }

    @Test
    void saveChapter_savesChapterAndUpdatesStory() {
        ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setStoryId(1L);
        chapterDTO.setChapterNo(1);
        chapterDTO.setUpdated(ZonedDateTime.now());

        Chapter chapter = new Chapter();
        Story story = new Story();
        chapter.setStory(story);
        chapter.setChapterNo(1);

        when(mapper.chapterDTOToChapter(chapterDTO)).thenReturn(chapter);
        when(chapterRepository.save(chapter)).thenReturn(chapter);

        editChapterService.saveChapter(chapterDTO);

        verify(chapterRepository, times(1)).save(chapter);
        verify(storyRepository, times(1)).save(story);
        verify(emailService, times(1)).sendMessageAboutNewStoryToQueue(story);
    }

    @Test
    void saveChapter_sendsMessageForNewChapter() {
        ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setStoryId(1L);
        chapterDTO.setChapterNo(2);
        chapterDTO.setUpdated(ZonedDateTime.now());

        Chapter chapter = new Chapter();
        Story story = new Story();
        chapter.setStory(story);

        when(mapper.chapterDTOToChapter(chapterDTO)).thenReturn(chapter);
        when(chapterRepository.save(chapter)).thenReturn(chapter);

        editChapterService.saveChapter(chapterDTO);

        verify(chapterRepository, times(1)).save(chapter);
        verify(storyRepository, times(1)).save(story);
        verify(emailService, times(1)).sendMessageAboutNewChapterToQueue(chapter);
    }

    @Test
    void editChapter_updatesExistingChapter() {
        ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setId(1L);
        chapterDTO.setUpdated(ZonedDateTime.now());
        chapterDTO.setChapterTitle("Updated Title");
        chapterDTO.setContent("Updated Content");

        Chapter chapter = new Chapter();
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        editChapterService.editChapter(chapterDTO);

        assertEquals("Updated Title", chapter.getChapterTitle());
        assertEquals("Updated Content", chapter.getContent());
        verify(chapterRepository, times(1)).save(chapter);
    }

    @Test
    void deleteChaptersForStory_deletesAllChaptersForStory() {
        Long storyId = 1L;

        editChapterService.deleteChaptersForStory(storyId);

        verify(chapterRepository, times(1)).deleteAllByStoryId(storyId);
    }

    @Test
    void deleteChapter_deletesChapterAndComments() {
        Long chapterId = 1L;

        editChapterService.deleteChapter(chapterId);

        verify(commentService, times(1)).deleteByChapterId(chapterId);
        verify(chapterRepository, times(1)).deleteById(chapterId);
    }

    @Test
    void editChaptersOrder_updatesChaptersOrder() {
        Story story = new Story();
        story.setId(1L);
        Chapter chapter1 = new Chapter();
        chapter1.setId(1L);
        chapter1.setStory(story);
        chapter1.setChapterTitle("Title 1");
        chapter1.setChapterNo(1);
        Chapter chapter2 = new Chapter();
        chapter2.setStory(story);
        chapter2.setChapterTitle("Title 2");
        chapter2.setId(2L);
        chapter2.setChapterNo(2);


        ChapterInfo chapterInfo1 = ChapterInfo.from(chapter1);
        ChapterInfo chapterInfo2 = ChapterInfo.from(chapter2);
        List<ChapterInfo> chapters = List.of(chapterInfo1, chapterInfo2);

        when(chapterRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(chapter1, chapter2));

        editChapterService.editChaptersOrder(chapters);

        assertEquals("Title 1", chapter1.getChapterTitle());
        assertEquals(1, chapter1.getChapterNo());
        assertEquals("Title 2", chapter2.getChapterTitle());
        assertEquals(2, chapter2.getChapterNo());
        verify(chapterRepository, times(1)).saveAll(List.of(chapter1, chapter2));
    }

}