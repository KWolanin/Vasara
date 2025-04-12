package com.kai.Vasara.service.comment;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.kai.Vasara.controller.comment.CommentController;
import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.entity.comment.Comment;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.model.comment.CommentDTO;
import com.kai.Vasara.repository.author.AuthorRepository;
import com.kai.Vasara.repository.chapter.ChapterRepository;
import com.kai.Vasara.repository.comment.CommentRepository;
import com.kai.Vasara.repository.story.StoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

class CommentServiceTest {

    @Mock private CommentRepository commentRepository;
    @Mock private StoryRepository storyRepository;
    @Mock private ChapterRepository chapterRepository;
    @Mock private AuthorRepository authorRepository;

    @InjectMocks private CommentService commentService;

    private Story story;
    private Chapter chapter;
    private Author author;
    private Comment comment;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        story = new Story();
        story.setId(1L);
        story.setAllowComments(true);
        story.setAllowGuestComments(true);

        chapter = new Chapter();
        chapter.setId(1L);
        chapter.setStory(story);

        author = new Author();
        author.setUsername("author");
        author.setEmail("author@example.com");

        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
        comment.setStory(story);
        comment.setChapter(chapter);

        comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("Test comment");
        comment2.setStory(story);
        comment2.setChapter(chapter);
        comment2.setParent(comment);
    }

    @Test
    void addComment_whenAuthorIsPresent_shouldAddCommentForAuthor() {
        CommentController.CommentRequest request = new CommentController.CommentRequest();
        request.setStoryId(1L);
        request.setChapterId(1L);
        request.setContent("Test comment");
        request.setName("author");
        request.setEmail("author@example.com");

        when(storyRepository.findById(1L)).thenReturn(Optional.of(story));
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(authorRepository.findAuthorByUsername("author")).thenReturn(Optional.of(author));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.addComment(request);

        verify(commentRepository).save(any(Comment.class)); // Sprawdzamy, czy zapisano komentarz
    }

    @Test
    void addComment_whenGuestIsPresent_shouldAddCommentForGuest() {
        CommentController.CommentRequest request = new CommentController.CommentRequest();
        request.setStoryId(1L);
        request.setChapterId(1L);
        request.setContent("Test guest comment");
        request.setName("guest");
        request.setEmail("guest@example.com");

        when(storyRepository.findById(1L)).thenReturn(Optional.of(story));
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(authorRepository.findAuthorByUsername("guest")).thenReturn(Optional.empty());
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.addComment(request);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void addComment_whenParentCommentIsPresent_shouldSetParent() {
        CommentController.CommentRequest request = new CommentController.CommentRequest();
        request.setStoryId(1L);
        request.setChapterId(1L);
        request.setContent("Test comment");
        request.setParentId(2L);

        Comment parentComment = new Comment();
        parentComment.setId(2L);
        parentComment.setContent("Parent comment");

        when(storyRepository.findById(1L)).thenReturn(Optional.of(story));
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(commentRepository.findById(2L)).thenReturn(Optional.of(parentComment));

        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        commentService.addComment(request);

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(captor.capture());

        Comment savedComment = captor.getValue();

        assertNotNull(savedComment.getParent(), "Parent powinien być ustawiony");
        assertEquals(2L, savedComment.getParent().getId(), "Parent ID powinno wynosić 2");
    }

    @Test
    void getCommentsForChapter_shouldReturnCommentDTOList() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
        comment.setStory(story);
        comment.setChapter(chapter);

        when(commentRepository.findByChapterIdAndParentIsNull(1L)).thenReturn(Collections.singletonList(comment));

        List<CommentDTO> result = commentService.getCommentsForChapter(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test comment", result.get(0).getContent());
    }

    @Test
    void getPermissions_shouldReturnCommentPermissions() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        CommentService.CommentPermission permissions = commentService.getPermissions(1L);

        assertTrue(permissions.isCommentAllowed());
        assertTrue(permissions.isGuestCommentAllowed());
    }

    @Test
    void delete_shouldDeleteComment() {
        long commentId = 1L;
        doNothing().when(commentRepository).deleteById(commentId);

        commentService.delete(commentId);

        verify(commentRepository).deleteById(commentId); // Sprawdzamy, czy usunięto komentarz
    }

    @Test
    void deleteByStoryId_shouldDeleteComments() {
        long storyId = 1L;
        doNothing().when(commentRepository).deleteAllByStoryId(storyId);

        commentService.deleteByStoryId(storyId);

        verify(commentRepository).deleteAllByStoryId(storyId); // Sprawdzamy, czy usunięto komentarze
    }

    @Test
    void deleteByChapterId_shouldDeleteComments() {
        long chapterId = 1L;
        doNothing().when(commentRepository).deleteAllByChapterId(chapterId);

        commentService.deleteByChapterId(chapterId);

        verify(commentRepository).deleteAllByChapterId(chapterId); // Sprawdzamy, czy usunięto komentarze
    }
}
