package com.kai.Vasara.repository;

import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.FollowingAuthors;
import com.kai.Vasara.entity.FollowingStories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowingAuthorRepository extends JpaRepository<FollowingAuthors, Long> {

    Optional<FollowingAuthors> findByFollowingAuthor_IdAndFollowedAuthor_Id(long followingAuthorId, long followedAuthorId);

    List<FollowingAuthors> findAllByFollowedAuthor_Id(long authorId);

    boolean existsByFollowingAuthor_IdAndFollowedAuthor_Id(long followingAuthorId, long followedAuthorId);

    int countByFollowingAuthor_Id(long id);
    Page<FollowingAuthors> findAllByFollowingAuthor(Author author, Pageable pageable);
}

