package com.kai.Vasara.repository;

import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.ReadStories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReadRepository extends JpaRepository<ReadStories, Long> {
    Optional<ReadStories> findByAuthorIdAndStoryId(long authorId, long storyId);

    boolean existsByAuthorIdAndStoryId(long authorId, long storyId);

    Page<ReadStories> findByAuthor(Author author, Pageable pageable);

    void deleteByStoryId(long id);

    int countByAuthorId(long id);
}
