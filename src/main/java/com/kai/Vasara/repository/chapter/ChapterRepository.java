package com.kai.Vasara.repository.chapter;

import com.kai.Vasara.entity.chapter.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    int countByStoryId(Long storyId);

//    @Query("SELECT c FROM Chapter c WHERE c.story.id = :storyId and chapterNo = :chapterNo")
    Optional<Chapter> findByStoryIdAndChapterNo(Long storyId, Long chapterNo);

    @Modifying
//    @Query("DELETE FROM Chapter c where c.story.id = :storyId")
    void deleteAllByStoryId(Long storyId);


    @Query("SELECT new Chapter(c.id, c.chapterNo, c.chapterTitle, NULL, c.story.id, published, updated) " +
            "FROM Chapter c WHERE c.story.id = :storyId")
    List<Chapter> findChaptersDataByStoryId(Long storyId);

}
