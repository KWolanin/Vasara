package com.kai.Vasara.repository;

import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.FollowingStories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowingRepository extends JpaRepository<FollowingStories, Long> {
    Optional<FollowingStories> findByAuthorIdAndStoryId(long authorId, long storyId);

    boolean existsByAuthorIdAndStoryId(long authorId, long storyId);

    Page<FollowingStories> findByAuthor(Author author, Pageable pageable);

    List<FollowingStories> findByStoryId(long storyId);
}
