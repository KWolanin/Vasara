package com.kai.Vasara.service;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.story.FollowStory;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.exception.author.AuthorException;
import com.kai.Vasara.exception.story.StoryException;
import com.kai.Vasara.repository.author.AuthorRepository;
import com.kai.Vasara.repository.story.FollowStoryRepository;
import com.kai.Vasara.repository.story.StoryRepository;
import com.kai.Vasara.service.story.FollowServiceStory;
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
class FollowingServiceTest {

    @InjectMocks
    FollowServiceStory followingService;

    @Mock
    FollowStoryRepository followStoryRepository;

    @Mock
    AuthorRepository authorRepository;

    @Mock
    StoryRepository storyRepository;


    @Test
    void add_exists_in_reads_list_remove_from_list() {
        long authorId = 1L;
        long storyId = 1L;

        FollowStory existingOne = new FollowStory();
        existingOne.setId(2L);
        when(followingService.check(authorId, storyId)).thenReturn(Optional.of(existingOne));

        followingService.add(authorId, storyId);

        verify(followStoryRepository, atMostOnce()).delete(existingOne);
        verify(followStoryRepository, never()).save(any());
    }

    @Test
    void add_no_author_throws_author_not_found() {
        long authorId = 1L;
        long storyId = 1L;

        when(followingService.check(authorId, storyId)).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        AuthorException exception = assertThrows(AuthorException.class, () -> {
            followingService.add(authorId, storyId);
        });
        assertEquals("User not found", exception.getAuthorError().getMessage());
        verifyNoMoreInteractions(followStoryRepository);
    }

    @Test
    void add_no_story_throws_story_not_found() {
        long authorId = 1L;
        long storyId = 1L;

        when(followingService.check(authorId, storyId)).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.of(new Author()));
        when(storyRepository.findById(1L)).thenReturn(Optional.empty());

        StoryException exception = assertThrows(StoryException.class, () -> {
            followingService.add(authorId, storyId);
        });

        assertEquals("Story was not found", exception.getStoryError().getMessage());
        verifyNoMoreInteractions(followStoryRepository);
    }

    @Test
    void add_success() {
        long authorId = 1L;
        long storyId = 1L;

        Author author = new Author();
        Story story = new Story();

        when(followingService.check(authorId, storyId)).thenReturn(Optional.empty());
        doReturn(Optional.of(author)).when(authorRepository).findById(1L);
        doReturn(Optional.of(story)).when(storyRepository).findById(1L);

        followingService.add(authorId, storyId);
        verify(followStoryRepository, atMostOnce()).save(any());
    }
}