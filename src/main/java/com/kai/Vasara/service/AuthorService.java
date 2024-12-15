package com.kai.Vasara.service;


import com.kai.Vasara.entity.Author;
import com.kai.Vasara.model.AuthorDAO;
import com.kai.Vasara.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        authors.forEach(story -> {
            daos.add(from(story));
        });
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
        Optional<Author> author = authorRepository.findAutorByUsername(username);
        if (author.isPresent()) return author.get().getId();
        return -1;
    }

    public AuthorDAO from(Author author) {
        AuthorDAO authorDAO = new AuthorDAO();
        authorDAO.setId(author.getId());
        authorDAO.setUsername(author.getUsername());
        return authorDAO;
    }
    public Author from(AuthorDAO authorDAO) {
        Author author = new Author();
        author.setId(authorDAO.getId());
        author.setUsername(authorDAO.getUsername());
        return author;
    }

    public Author register(String username, String login, String rawPassword) {
        Author author = new Author();
        author.setLogin(login);
        author.setUsername(username);
        author.setPassword(passwordEncoder.encode(rawPassword));
        return authorRepository.save(author);
    }

    public Author authenticate(String login, String rawPassword) {
        return authorRepository.findByLogin(login)
                .filter(author -> passwordEncoder.matches(rawPassword, author.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid login or password"));
    }

}
