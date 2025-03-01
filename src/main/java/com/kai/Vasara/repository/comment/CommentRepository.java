package com.kai.Vasara.repository.comment;

import com.kai.Vasara.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByChapterIdAndParentIsNull(Long chapterId);

    List<Comment> findByChapterId(Long chapterId);
}
