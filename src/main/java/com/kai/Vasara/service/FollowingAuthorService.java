package com.kai.Vasara.service;

import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.FollowingAuthors;
import com.kai.Vasara.exception.AuthorError;
import com.kai.Vasara.exception.AuthorException;
import com.kai.Vasara.repository.AuthorRepository;
import com.kai.Vasara.repository.FollowingAuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Slf4j
public class FollowingAuthorService implements AuthorActionService<FollowingAuthors> {


    private final FollowingAuthorRepository followingAuthorRepository;
    private final AuthorRepository authorRepository;

    public FollowingAuthorService(FollowingAuthorRepository followingAuthorRepository, AuthorRepository authorRepository) {
        this.followingAuthorRepository = followingAuthorRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Optional<FollowingAuthors> check(long followingAuthorId, long followedAuthorId) {
        return followingAuthorRepository.findByFollowingAuthor_IdAndFollowedAuthor_Id(followingAuthorId, followedAuthorId);
    }

    @Override
    public boolean add(long followingAuthorId, long followedAuthorId) {
        if (followingAuthorId == followedAuthorId) {
            throw new AuthorException(AuthorError.CANNOT_FOLLOW_ITSELF);
        }
        Optional<FollowingAuthors> follow = check(followingAuthorId, followedAuthorId);
        if (follow.isPresent()) {
            log.warn("Author is removed from follows");
            followingAuthorRepository.delete(follow.get());
            return false;
        }
        Author followingAuthor = authorRepository.findById(followingAuthorId)
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
        Author followedAuthor = authorRepository.findById(followedAuthorId)
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
        FollowingAuthors fa = new FollowingAuthors();
        fa.setFollowingAuthor(followingAuthor);
        fa.setFollowedAuthor(followedAuthor);
        fa.setAddedAt(ZonedDateTime.now());
        followingAuthorRepository.save(fa);
        return true;
    }

    @Override
    public Boolean is(long followingAuthorId, long followedAuthorId) {
        return followingAuthorRepository.existsByFollowingAuthor_IdAndFollowedAuthor_Id(followingAuthorId, followedAuthorId);
    }
}
