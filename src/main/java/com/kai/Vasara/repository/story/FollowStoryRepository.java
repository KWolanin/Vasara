package com.kai.Vasara.repository.story;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.story.FollowStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowStoryRepository extends JpaRepository<FollowStory, Long> {
    Optional<FollowStory> findByAuthorIdAndStoryId(long authorId, long storyId);

    boolean existsByAuthorIdAndStoryId(long authorId, long storyId);

    Page<FollowStory> findByAuthor(Author author, Pageable pageable);

    List<FollowStory> findByStoryId(long storyId);

    void deleteByStoryId(long id);

    int countByAuthorId(long id);
}
