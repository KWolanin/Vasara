package com.kai.Vasara.repository;

import com.kai.Vasara.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    Page<Story> findAllByAuthorId(Long id, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Story s where s.id = :id")
    int deleteStoryById(Long id);

    @Query("select count(*) from Story s where s.authorId = :id")
    long countMine(Long id);
}
