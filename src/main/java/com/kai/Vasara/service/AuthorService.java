package com.kai.Vasara.service;


import com.kai.Vasara.entity.Author;
import com.kai.Vasara.model.AuthorDAO;
import com.kai.Vasara.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AuthorService(AuthorRepository authorRepository, PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
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

    public long getAuthorIdByName(String username) {
        Optional<Author> author = authorRepository.findAuthorByUsername(username);
        if (author.isPresent()) return author.get().getId();
        return -1;
    }

    public AuthorDAO from(Author author) {
        AuthorDAO authorDAO = new AuthorDAO();
        authorDAO.setId(author.getId());
        authorDAO.setUsername(author.getUsername());
        authorDAO.setEmail(author.getEmail());
        return authorDAO;
    }
    public Author from(AuthorDAO authorDAO) {
        Author author = new Author();
        author.setId(authorDAO.getId());
        author.setUsername(authorDAO.getUsername());
        author.setEmail(authorDAO.getEmail());
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
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Email is incorrect");
        }
        Author author = new Author();
        author.setLogin(login);
        author.setUsername(username);
        author.setPassword(passwordEncoder.encode(rawPassword));
        author.setEmail(email);
        return authorRepository.save(author);
    }

    private boolean validateEmail(String email) {
        String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

        if (StringUtils.hasText(email)) {
            return EMAIL_PATTERN.matcher(email).matches();
        }
        return false;
    }


    private boolean validateUsername(String username) {
        String USERNAME_REGEX = "^(?=.{3,20}$)(?![_\\.\\-])[A-Za-z0-9._-]+(?<![_\\.\\-])$";
        Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);
        if (!StringUtils.hasText(username)) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
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

    public boolean changeEmail(String email, long id) {
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
       Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            author.get().setEmail(email);
            return authorRepository.save(author.get()).getId() > 0;
        }
        return false;
    }

    public boolean changeUsername(String username, long id) {
        if (!validateUsername(username)) {
            throw new IllegalArgumentException("Invalid username");
        }
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            author.get().setUsername(username);
            return authorRepository.save(author.get()).getId() > 0;
        }
        return false;
    }
}
