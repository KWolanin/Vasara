package com.kai.Vasara.service.author;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.exception.author.AuthorError;
import com.kai.Vasara.exception.author.AuthorException;
import com.kai.Vasara.model.author.AuthorDTO;
import com.kai.Vasara.repository.author.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Component
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, PasswordEncoder passwordEncoder, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDTO> getAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::from)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthor(Long id) {
        return authorRepository.findById(id).map(authorMapper::from)
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
    }

    public void saveAuthor(Author author) {
        authorRepository.save(author);
    }

    public String getAuthorName(Long id) {
        return authorRepository.findUsernameById(id)
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_DETAILS_NOT_FOUND));
    }

    public String getAuthorDesc(Long id) {
        return authorRepository.findDescriptionById(id)
                        .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_DETAILS_NOT_FOUND));
    }


    public Author register(Author author) {
        if (authorRepository.existsByLogin(author.getLogin())) {
            throw new AuthorException(AuthorError.AUTHOR_LOGIN_EXISTS);
        }
        if (authorRepository.existsByUsername(author.getUsername())) {
            throw new AuthorException(AuthorError.AUTHOR_USERNAME_EXISTS);
        }
        if (authorRepository.existsByEmail(author.getEmail())) {
            throw new AuthorException(AuthorError.AUTHOR_EMAIL_EXISTS);
        }
        if (!emailValid(author.getEmail())) {
            throw new AuthorException(AuthorError.AUTHOR_EMAIL_INCORRECT);
        }
        author.setPassword(passwordEncoder.encode(author.getPassword()));
        return authorRepository.save(author);
    }

    private boolean emailValid(String email) {
        String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

        if (StringUtils.hasText(email)) {
            return EMAIL_PATTERN.matcher(email).matches();
        }
        return false;
    }


    public AuthorDTO authenticate(String login, String rawPassword) {
        Author author = authorRepository.findByLogin(login)
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
        if (!passwordEncoder.matches(rawPassword, author.getPassword())) {
            throw new AuthorException(AuthorError.AUTHOR_INVALID_CREDENTIALS);
        }
        return authorMapper.from(author);
    }




    public Optional<Author> find(long authorId) {
        return authorRepository.findById(authorId);
    }

    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true)
    public void updateAuthor(Author author) {
        Author authorInDb = authorRepository.findById(author.getId())
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));

        if (author.getEmail() != null) {
            if (!emailValid(author.getEmail())) {
                throw new AuthorException(AuthorError.AUTHOR_INVALID_EMAIL);
            }
            if (authorRepository.existsByEmail(author.getEmail()) && !authorInDb.getEmail().equals(author.getEmail())) {
                throw new AuthorException(AuthorError.AUTHOR_EMAIL_EXISTS);
            }
            authorInDb.setEmail(author.getEmail());
        }
        if (author.getUsername() != null) {
            if (authorRepository.existsByUsername(author.getUsername())) {
                throw new AuthorException(AuthorError.AUTHOR_USERNAME_EXISTS);
            }
            authorInDb.setUsername(author.getUsername());
        }
        if (author.getPassword() != null) {
            if (!StringUtils.hasText(author.getPassword())) {
                throw new AuthorException(AuthorError.AUTHOR_INVALID_PASSWORD);
            }
            authorInDb.setPassword(passwordEncoder.encode(author.getPassword()));
        }
        if (author.getDescription() != null) {
            authorInDb.setDescription(author.getDescription());
        }

        authorRepository.save(authorInDb);
    }

}
