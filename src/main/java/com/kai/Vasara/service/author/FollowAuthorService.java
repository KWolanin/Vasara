package com.kai.Vasara.service.author;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.author.FollowAuthor;
import com.kai.Vasara.exception.author.AuthorError;
import com.kai.Vasara.exception.author.AuthorException;
import com.kai.Vasara.repository.author.AuthorRepository;
import com.kai.Vasara.repository.author.FollowAuthorRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FollowAuthorService implements AuthorActionService<FollowAuthor> {


    private final FollowAuthorRepository followAuthorRepository;
    private final AuthorRepository authorRepository;

    public FollowAuthorService(FollowAuthorRepository followAuthorRepository, AuthorRepository authorRepository) {
        this.followAuthorRepository = followAuthorRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Optional<FollowAuthor> check(long followingAuthorId, long followedAuthorId) {
        return followAuthorRepository.findByFollowingAuthor_IdAndFollowedAuthor_Id(followingAuthorId, followedAuthorId);
    }

    @Override
    public boolean add(long followingAuthorId, long followedAuthorId) {
        if (followingAuthorId == followedAuthorId) {
            throw new AuthorException(AuthorError.CANNOT_FOLLOW_ITSELF);
        }
        Optional<FollowAuthor> follow = check(followingAuthorId, followedAuthorId);
        if (follow.isPresent()) {
            log.warn("Author is removed from follows");
            followAuthorRepository.delete(follow.get());
            return false;
        }
        Author followingAuthor = authorRepository.findById(followingAuthorId)
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
        Author followedAuthor = authorRepository.findById(followedAuthorId)
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
        FollowAuthor fa = new FollowAuthor();
        fa.setFollowingAuthor(followingAuthor);
        fa.setFollowedAuthor(followedAuthor);
        fa.setAddedAt(ZonedDateTime.now());
        followAuthorRepository.save(fa);
        return true;
    }

    @Override
    public Boolean is(long followingAuthorId, long followedAuthorId) {
        return followAuthorRepository.existsByFollowingAuthor_IdAndFollowedAuthor_Id(followingAuthorId, followedAuthorId);
    }

    public int count(long id) {
        return followAuthorRepository.countByFollowingAuthor_Id(id);
    }

    public Page<AuthorInfo> get(int page, int size, long id) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);
        return authorRepository.findById(id)
                .map(author -> {
                    Page<FollowAuthor> followAuthors = followAuthorRepository.findAllByFollowingAuthor(author, pageable);
                    List<AuthorInfo> daos = followAuthors.stream().map(a -> {
                        AuthorInfo ai = new AuthorInfo();
                        ai.setAuthorId(a.getFollowedAuthor().getId());
                        ai.setAuthorUsername(a.getFollowedAuthor().getUsername());
                        return ai;
                    }).toList();
                    return new PageImpl<>(daos, pageable, followAuthors.getTotalElements());
                })
                .orElseGet(() -> new PageImpl<>(List.of(), pageable, 0));
    }


    @Getter
    @Setter
    public static class AuthorInfo {
        private String authorUsername;
        private Long authorId;
    }
}
