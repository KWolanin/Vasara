package com.kai.Vasara.service.comment;

import com.kai.Vasara.controller.comment.CommentController;
import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.entity.comment.Comment;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.exception.chapter.ChapterError;
import com.kai.Vasara.exception.chapter.ChapterException;
import com.kai.Vasara.exception.comment.CommentError;
import com.kai.Vasara.exception.comment.CommentException;
import com.kai.Vasara.exception.story.StoryError;
import com.kai.Vasara.exception.story.StoryException;
import com.kai.Vasara.model.comment.CommentDTO;
import com.kai.Vasara.repository.author.AuthorRepository;
import com.kai.Vasara.repository.chapter.ChapterRepository;
import com.kai.Vasara.repository.comment.CommentRepository;
import com.kai.Vasara.repository.story.StoryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final StoryRepository storyRepository;
    private final ChapterRepository chapterRepository;
    private final AuthorRepository authorRepository;

    public void addComment(CommentController.CommentRequest request) {
        Story story = storyRepository.findById(request.getStoryId())
                .orElseThrow(() -> new StoryException(StoryError.STORY_NOT_FOUND));

        Chapter chapter = chapterRepository.findById(request.getChapterId())
                .orElseThrow(() -> new ChapterException(ChapterError.CHAPTER_NOT_FOUND));
        Comment comment = new Comment();
        Comment parentComment;
        if (request.getParentId() != null && request.getParentId() > 0) {
            parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CommentException(CommentError.PARENT_NOT_FOUND));
            comment.setParent(parentComment);
        }
        comment.setStory(story);
        comment.setChapter(chapter);
        comment.setContent(request.getContent());

        Optional<Author> author = authorRepository.findAuthorByUsername(request.getName());
        if (author.isPresent()) {
            comment.setAuthor(author.get());
        } else {
            comment.setGuestName(request.getName());
            comment.setGuestEmail(request.getEmail());
        }
        commentRepository.save(comment);
    }

    public List<CommentDTO> getCommentsForChapter(Long chapterId) {
        return commentRepository.findByChapterIdAndParentIsNull(chapterId)
                .stream().map(this::from).collect(Collectors.toList());
    }


    private CommentDTO from(Comment comment) {
        String authorName = comment.getAuthor() != null ? comment.getAuthor().getUsername() : comment.getGuestName();
        String email = comment.getAuthor() != null ? comment.getAuthor().getEmail() : comment.getGuestEmail();
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setStoryId(comment.getStory().getId());
        commentDTO.setChapterId(comment.getChapter().getId());
        commentDTO.setName(authorName);
        commentDTO.setEmail(email);
        commentDTO.setCreatedAt(comment.getCreatedAt());
        if (Objects.nonNull(comment.getParent())) {
            commentDTO.setParentId(comment.getParent().getId());
        }
        List<CommentDTO> replies = getReplies(comment);
        if (!replies.isEmpty()) {
            commentDTO.setReplies(replies);
        }
        return commentDTO;
    }

    private List<CommentDTO> getReplies(Comment comment) {
        return comment.getReplies().stream()
                .map(this::from)
                .toList();
    }

    public CommentPermission getPermissions(long chapterId) {
        CommentPermission commentPermission = new CommentPermission();
        Optional<Chapter> chapter = chapterRepository.findById(chapterId);
        if (chapter.isPresent()) {
            commentPermission.setCommentAllowed(chapter.get().getStory().isAllowComments());
            commentPermission.setGuestCommentAllowed(chapter.get().getStory().isAllowGuestComments());
        }
        return commentPermission;
    }

    public void delete(long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Data
    public static class CommentPermission {
        private boolean commentAllowed;
        private boolean guestCommentAllowed;
    }
}
