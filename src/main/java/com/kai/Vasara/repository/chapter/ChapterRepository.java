package com.kai.Vasara.repository.chapter;

import com.kai.Vasara.entity.chapter.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    int countByStoryId(Long storyId);

    Chapter findByStoryIdAndChapterNo(Long storyId, Long chapterNo);

    boolean existsByStoryIdAndChapterNo(Long storyId, Long chapterNo);

    @Modifying
    void deleteAllByStoryId(Long storyId);

    List<Chapter> findChaptersDataByStoryId(Long storyId);

}
