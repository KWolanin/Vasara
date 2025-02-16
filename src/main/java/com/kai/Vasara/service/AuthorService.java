package com.kai.Vasara.service;


import com.kai.Vasara.controller.AuthorController;
import com.kai.Vasara.entity.Author;
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

    public Author register(String username, String login, String rawPassword, String email) {
        if (authorRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("User with this login already exists");
        }
        if (authorRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("User with this username already exists");
        }
        if (authorRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        if (!emailValid(email)) {
            throw new IllegalArgumentException("Email is incorrect");
        }
        Author author = new Author();
        author.setLogin(login);
        author.setUsername(username);
        author.setPassword(passwordEncoder.encode(rawPassword));
        author.setEmail(email);
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
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!passwordEncoder.matches(rawPassword, author.getPassword())) {
            throw new IllegalArgumentException("Invalid login or password");
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
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        if (request.getEmail() != null) {
            if (!emailValid(request.getEmail())) {
                throw new IllegalArgumentException("Invalid email");
            }
            if (authorRepository.findByEmail(request.getEmail() ).isPresent()) {
                throw new IllegalArgumentException("User with this email already exists");
            }
            author.setEmail(request.getEmail());
        }
        if (request.getUsername() != null) {
            if (!usernameValid(request.getUsername())) {
                throw new IllegalArgumentException("Invalid username");
            }
            if (authorRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new IllegalArgumentException("User with this username already exists");
            }
            author.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            if (!StringUtils.hasText(request.getPassword())) {
                throw new IllegalArgumentException("Invalid password");
            }
            author.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getDescription() != null) {
            if (!StringUtils.hasText(request.getDescription())) {
                throw new IllegalArgumentException("Invalid description");
            }
            author.setDescription(request.getDescription());
        }

        authorRepository.save(author);
        return true;
    }

}
