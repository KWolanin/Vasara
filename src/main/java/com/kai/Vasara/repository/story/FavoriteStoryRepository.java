package com.kai.Vasara.repository.story;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.story.FavoriteStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteStoryRepository extends JpaRepository<FavoriteStory, Long> {
    Optional<FavoriteStory> findByAuthorIdAndStoryId(long authorId, long storyId);

    boolean existsByAuthorIdAndStoryId(long authorId, long storyId);

    Page<FavoriteStory> findByAuthor(Author author, Pageable pageable);

    void deleteByStoryId(long id);

    int countByAuthorId(long id);
}
