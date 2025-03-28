package com.kai.Vasara.repository.story;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.story.ReadLaterStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReadLaterRepository extends JpaRepository<ReadLaterStory, Long> {
    Optional<ReadLaterStory> findByAuthorIdAndStoryId(long authorId, long storyId);

    boolean existsByAuthorIdAndStoryId(long authorId, long storyId);

    Page<ReadLaterStory> findByAuthor(Author author, Pageable pageable);

    void deleteByStoryId(long id);

    int countByAuthorId(long id);
}
