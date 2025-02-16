package com.kai.Vasara.service;

import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.FavoriteStories;
import com.kai.Vasara.entity.FollowingStories;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.repository.AuthorRepository;
import com.kai.Vasara.repository.FavoriteRepository;
import com.kai.Vasara.repository.FollowingRepository;
import com.kai.Vasara.repository.StoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atMostOnce;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @InjectMocks
    FavoriteService favoriteService;

    @Mock
    FavoriteRepository favoriteRepository;

    @Mock
    AuthorRepository authorRepository;

    @Mock
    StoryRepository storyRepository;


    @Test
    void add_exists_in_reads_list_remove_from_list() {
        long authorId = 1L;
        long storyId = 1L;

        FavoriteStories existingOne = new FavoriteStories();
        existingOne.setId(2L);
        when(favoriteService.check(authorId, storyId)).thenReturn(Optional.of(existingOne));

        favoriteService.add(authorId, storyId);

        verify(favoriteRepository, atMostOnce()).delete(existingOne);
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void add_no_author_throws_EntityNotFoundException() {
        long authorId = 1L;
        long storyId = 1L;

        when(favoriteService.check(authorId, storyId)).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            favoriteService.add(authorId, storyId);
        });
        assertEquals("Author not found", exception.getMessage());
        verifyNoMoreInteractions(favoriteRepository);
    }

    @Test
    void add_no_story_throws_EntityNotFoundException() {
        long authorId = 1L;
        long storyId = 1L;

        when(favoriteService.check(authorId, storyId)).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));
        when(storyRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            favoriteService.add(authorId, storyId);
        });

        assertEquals("Story not found", exception.getMessage());
        verifyNoMoreInteractions(favoriteRepository);
    }

    @Test
    void add_success() {
        long authorId = 1L;
        long storyId = 1L;

        Author author = new Author();
        Story story = new Story();

        when(favoriteService.check(authorId, storyId)).thenReturn(Optional.empty());
        doReturn(Optional.of(author)).when(authorRepository).findById(1L);
        doReturn(Optional.of(story)).when(storyRepository).findById(1L);

        favoriteService.add(authorId, storyId);
        verify(favoriteRepository, atMostOnce()).save(any());
    }
}