package com.kai.Vasara.service.author;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.author.FollowAuthor;
import com.kai.Vasara.exception.author.AuthorError;
import com.kai.Vasara.exception.author.AuthorException;
import com.kai.Vasara.repository.author.AuthorRepository;
import com.kai.Vasara.repository.author.FollowAuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FollowAuthorServiceTest {

    @Mock
    private FollowAuthorRepository followAuthorRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private FollowAuthorService followAuthorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheck() {
        long followingAuthorId = 1L;
        long followedAuthorId = 2L;

        FollowAuthor followAuthor = new FollowAuthor();
        when(followAuthorRepository.findByFollowingAuthor_IdAndFollowedAuthor_Id(followingAuthorId, followedAuthorId))
                .thenReturn(Optional.of(followAuthor));

        Optional<FollowAuthor> result = followAuthorService.check(followingAuthorId, followedAuthorId);

        assertTrue(result.isPresent());
        verify(followAuthorRepository, times(1))
                .findByFollowingAuthor_IdAndFollowedAuthor_Id(followingAuthorId, followedAuthorId);
    }

    @Test
    void testAdd_Success() {
        long followingAuthorId = 1L;
        long followedAuthorId = 2L;

        Author followingAuthor = new Author();
        followingAuthor.setId(followingAuthorId);

        Author followedAuthor = new Author();
        followedAuthor.setId(followedAuthorId);

        when(followAuthorRepository.findByFollowingAuthor_IdAndFollowedAuthor_Id(followingAuthorId, followedAuthorId))
                .thenReturn(Optional.empty());
        when(authorRepository.findById(followingAuthorId)).thenReturn(Optional.of(followingAuthor));
        when(authorRepository.findById(followedAuthorId)).thenReturn(Optional.of(followedAuthor));

        boolean result = followAuthorService.add(followingAuthorId, followedAuthorId);

        assertTrue(result);
        ArgumentCaptor<FollowAuthor> captor = ArgumentCaptor.forClass(FollowAuthor.class);
        verify(followAuthorRepository, times(1)).save(captor.capture());
        assertEquals(followingAuthor, captor.getValue().getFollowingAuthor());
        assertEquals(followedAuthor, captor.getValue().getFollowedAuthor());
    }

    @Test
    void testAdd_CannotFollowItself() {
        long authorId = 1L;

        AuthorException exception = assertThrows(AuthorException.class, () -> followAuthorService.add(authorId, authorId));

        assertEquals(AuthorError.CANNOT_FOLLOW_ITSELF, exception.getAuthorError());
    }

    @Test
    void testIs() {
        long followingAuthorId = 1L;
        long followedAuthorId = 2L;

        when(followAuthorRepository.existsByFollowingAuthor_IdAndFollowedAuthor_Id(followingAuthorId, followedAuthorId))
                .thenReturn(true);

        boolean result = followAuthorService.is(followingAuthorId, followedAuthorId);

        assertTrue(result);
        verify(followAuthorRepository, times(1))
                .existsByFollowingAuthor_IdAndFollowedAuthor_Id(followingAuthorId, followedAuthorId);
    }

    @Test
    void testCount() {
        long authorId = 1L;

        when(followAuthorRepository.countByFollowingAuthor_Id(authorId)).thenReturn(5);

        int result = followAuthorService.count(authorId);

        assertEquals(5, result);
        verify(followAuthorRepository, times(1)).countByFollowingAuthor_Id(authorId);
    }

    @Test
    void testGet() {
        long authorId = 1L;
        int page = 1;
        int size = 10;

        Author author = new Author();
        author.setId(authorId);

        FollowAuthor followAuthor = new FollowAuthor();
        Author followedAuthor = new Author();
        followedAuthor.setId(2L);
        followedAuthor.setUsername("testUser");
        followAuthor.setFollowedAuthor(followedAuthor);

        PageRequest pageable = PageRequest.of(0, size);
        Page<FollowAuthor> followAuthorPage = new PageImpl<>(List.of(followAuthor), pageable, 1);

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(followAuthorRepository.findAllByFollowingAuthor(author, pageable)).thenReturn(followAuthorPage);

        Page<FollowAuthorService.AuthorInfo> result = followAuthorService.get(page, size, authorId);

        assertEquals(1, result.getTotalElements());
        assertEquals("testUser", result.getContent().get(0).getAuthorUsername());
        verify(authorRepository, times(1)).findById(authorId);
        verify(followAuthorRepository, times(1)).findAllByFollowingAuthor(author, pageable);
    }
}