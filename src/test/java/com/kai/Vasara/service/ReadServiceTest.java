package com.kai.Vasara.service;

import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.ReadStories;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.exception.AuthorException;
import com.kai.Vasara.exception.StoryException;
import com.kai.Vasara.repository.AuthorRepository;
import com.kai.Vasara.repository.ReadRepository;
import com.kai.Vasara.repository.StoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadServiceTest {

    @InjectMocks
    ReadServiceStory readService;

    @Mock
    ReadRepository readRepository;

    @Mock
    AuthorRepository authorRepository;

    @Mock
    StoryRepository storyRepository;


    @Test
    void add_exists_in_reads_list_remove_from_list() {
        long authorId = 1L;
        long storyId = 1L;

        ReadStories existingOne = new ReadStories();
        existingOne.setId(2L);
        when(readService.check(authorId, storyId)).thenReturn(Optional.of(existingOne));

        readService.add(authorId, storyId);

        verify(readRepository, atMostOnce()).delete(existingOne);
        verify(readRepository, never()).save(any());
    }

    @Test
    void add_no_author_throws_author_not_found() {
        long authorId = 1L;
        long storyId = 1L;

        when(readService.check(authorId, storyId)).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        AuthorException exception = assertThrows(AuthorException.class, () -> {
            readService.add(authorId, storyId);
        });
        assertEquals("User not found", exception.getAuthorError().getMessage());
        verifyNoMoreInteractions(readRepository);
    }

    @Test
    void add_no_story_throws_story_not_found() {
        long authorId = 1L;
        long storyId = 1L;

        when(readService.check(authorId, storyId)).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));
        when(storyRepository.findById(1L)).thenReturn(Optional.empty());

        StoryException exception = assertThrows(StoryException.class, () -> {
            readService.add(authorId, storyId);
        });

        assertEquals("Story was not found", exception.getStoryError().getMessage());
        verifyNoMoreInteractions(readRepository);
    }

    @Test
    void add_success() {
        long authorId = 1L;
        long storyId = 1L;

        Author author = new Author();
        Story story = new Story();

        when(readService.check(authorId, storyId)).thenReturn(Optional.empty());
        doReturn(Optional.of(author)).when(authorRepository).findById(1L);
        doReturn(Optional.of(story)).when(storyRepository).findById(1L);

        readService.add(authorId, storyId);
        verify(readRepository, atMostOnce()).save(any());
    }
}