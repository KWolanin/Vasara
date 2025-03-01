package com.kai.Vasara.repository.author;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.author.FollowAuthor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowAuthorRepository extends JpaRepository<FollowAuthor, Long> {

    Optional<FollowAuthor> findByFollowingAuthor_IdAndFollowedAuthor_Id(long followingAuthorId, long followedAuthorId);

    List<FollowAuthor> findAllByFollowedAuthor_Id(long authorId);

    boolean existsByFollowingAuthor_IdAndFollowedAuthor_Id(long followingAuthorId, long followedAuthorId);

    int countByFollowingAuthor_Id(long id);
    Page<FollowAuthor> findAllByFollowingAuthor(Author author, Pageable pageable);
}

