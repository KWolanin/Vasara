package com.kai.Vasara.repository;

import com.kai.Vasara.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findAllByAuthorId(Long id);

    @Modifying
    @Query("DELETE FROM Story s where s.id = :id")
    int deleteStoryById(Long id);
}
