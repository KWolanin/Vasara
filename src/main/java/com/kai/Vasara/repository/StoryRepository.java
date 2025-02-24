package com.kai.Vasara.repository;

import com.kai.Vasara.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    Page<Story> findAllByAuthorId(Long id, Pageable pageable);

    @Modifying
    int deleteStoryById(Long id);

    @Query("select count(*) from Story s where s.author.id = :id")
    long countMine(Long id);

    @Query("SELECT s FROM Story s WHERE s.chapters IS NOT EMPTY")
    Page<Story> findAllWithChapters(Pageable pageable);

    Page<Story> findAll(Specification<Story> spec, Pageable pageable);

    @Query("select count(*) from Story s where s.chapters IS NOT EMPTY")
    long count();
}
