package com.kai.Vasara.service;


import com.kai.Vasara.controller.AuthorController;
import com.kai.Vasara.entity.Author;
import com.kai.Vasara.exception.AuthorError;
import com.kai.Vasara.exception.AuthorException;
import com.kai.Vasara.model.AuthorDAO;
import com.kai.Vasara.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Component
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoryService storyService;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, PasswordEncoder passwordEncoder, @Lazy StoryService storyService) {
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
        this.storyService = storyService;
    }

    public List<AuthorDAO> getAll() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorDAO> daos = new ArrayList<>();
        authors.forEach(story -> daos.add(from(story)));
        return daos;
    }

    public AuthorDAO getAuthor(Long id) {
        Optional<Author> opt = authorRepository.findById(id);
        return opt.map(this::from).orElse(null);
    }

    public Boolean saveAuthor(Author author) {
        try {
            authorRepository.save(author);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getAuthorName(Long id) {
        Optional<String> opt = authorRepository.findUsernameById(id);
        return opt.orElse("");
    }

    public String getAuthorDesc(Long id) {
        Optional<String> opt = authorRepository.findDescriptionById(id);
        return opt.orElse("");
    }

    public AuthorDAO from(Author author) {
        AuthorDAO authorDAO = new AuthorDAO();
        authorDAO.setId(author.getId());
        authorDAO.setUsername(author.getUsername());
        authorDAO.setEmail(author.getEmail());
        authorDAO.setDescription(author.getDescription());
        authorDAO.setStories(new ArrayList<>());
        author.getStories().forEach(story -> authorDAO.getStories().add(storyService.from(story)));
        return authorDAO;
    }
    public Author from(AuthorDAO authorDAO) {
        Author author = new Author();
        author.setId(authorDAO.getId());
        author.setUsername(authorDAO.getUsername());
        author.setEmail(authorDAO.getEmail());
        author.setDescription(authorDAO.getDescription());
        author.setStories(new ArrayList<>());
        authorDAO.getStories().forEach(storyDAO -> author.getStories().add(storyService.from(storyDAO)));
        return author;
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


    private boolean usernameValid(String username) {
        String USERNAME_REGEX = "^(?=.{3,20}$)(?![_\\.\\-])[A-Za-z0-9._-]+(?<![_\\.\\-])$";
        Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);
        if (StringUtils.hasText(username)) {
            return USERNAME_PATTERN.matcher(username).matches();
        }
        return false;
    }

    public Author authenticate(String login, String rawPassword) {
        Author author = authorRepository.findByLogin(login)
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));
        if (!passwordEncoder.matches(rawPassword, author.getPassword())) {
            throw new AuthorException(AuthorError.AUTHOR_INVALID_CREDENTIALS);
        }
        return author;
    }


    public Optional<Author> find(long authorId) {
        return authorRepository.findById(authorId);
    }

    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    public boolean updateAuthor(AuthorController.UpdateAuthorRequest request) {
        Author author = authorRepository.findById(request.getId())
                .orElseThrow(() -> new AuthorException(AuthorError.AUTHOR_NOT_FOUND));

        if (request.getEmail() != null) {
            if (!emailValid(request.getEmail())) {
                throw new AuthorException(AuthorError.AUTHOR_INVALID_EMAIL);
            }
            if (authorRepository.existsByEmail(request.getEmail()) && !author.getEmail().equals(request.getEmail())) {
                throw new AuthorException(AuthorError.AUTHOR_EMAIL_EXISTS);
            }
            author.setEmail(request.getEmail());
        }
        if (request.getUsername() != null) {
            if (!usernameValid(request.getUsername())) {
                throw new AuthorException(AuthorError.AUTHOR_INVALID_USERNAME);
            }
            if (authorRepository.existsByUsername(request.getUsername())) {
                throw new AuthorException(AuthorError.AUTHOR_USERNAME_EXISTS);
            }
            author.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            if (!StringUtils.hasText(request.getPassword())) {
                throw new AuthorException(AuthorError.AUTHOR_INVALID_PASSWORD);
            }
            author.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getDescription() != null) {
            if (!StringUtils.hasText(request.getDescription())) {
                throw new AuthorException(AuthorError.AUTHOR_INVALID_DESCRIPTION);
            }
            author.setDescription(request.getDescription());
        }

        authorRepository.save(author);
        return true;
    }

    public boolean updateAuthor(Author author) {
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
            if (!StringUtils.hasText(author.getDescription())) {
                throw new AuthorException(AuthorError.AUTHOR_INVALID_DESCRIPTION);
            }
            authorInDb.setDescription(author.getDescription());
        }

        authorRepository.save(authorInDb);
        return true;
    }

}
