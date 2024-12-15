package com.kai.Vasara.controller;

import com.kai.Vasara.entity.Author;
import com.kai.Vasara.model.AuthorDAO;
import com.kai.Vasara.service.AuthorService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AuthorDAO>> getAuthors() {
        return new ResponseEntity<>(authorService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDAO> getAuthor(@PathVariable Long id) {
        return new ResponseEntity<>(authorService.getAuthor(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<String> getAuthorName(@PathVariable Long id) {
        return new ResponseEntity<>(authorService.getAuthorName(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.saveAuthor(author), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Author autor = authorService.register(request.getUsername(), request.getLogin(), request.getPassword());
        return ResponseEntity.ok("User registered successfully: " + autor.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Author autor = authorService.authenticate(request.getLogin(), request.getPassword());
        return ResponseEntity.ok(new LoggedUser(autor.getId(), autor.getLogin(), autor.getUsername()));
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String login;
        private String password;
    }

    @Data
    public static class LoginRequest {
        private String login;
        private String password;
    }

    @Data
    @AllArgsConstructor
    private static class LoggedUser {
        private Long id;
        private String login;
        private String username;
    }

}
