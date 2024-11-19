package com.kai.Vasara.service;


import com.kai.Vasara.entity.Author;
import com.kai.Vasara.model.AuthorDAO;
import com.kai.Vasara.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Component
public class AuthorService {

    private final AuthorRepository authorRepository;
    //private final StoryService storyService;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
       // this.storyService = storyService;
    }

    public List<Author> getAll() {
        return authorRepository.findAll();
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

    private AuthorDAO from(Author author) {
        AuthorDAO a = new AuthorDAO();

        a.setId(author.getId());
        a.setUsername(author.getUsername());

        // a.setChapters();
      //  a.setStories(storyService.getStoriesByAuthor(author.getId()));

        return a;
    }
}
