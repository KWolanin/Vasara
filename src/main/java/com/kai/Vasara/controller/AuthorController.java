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

    @GetMapping("/{id}/desc")
    public ResponseEntity<String> getAuthorDesc(@PathVariable Long id) {
        return new ResponseEntity<>(authorService.getAuthorDesc(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.saveAuthor(author), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            authorService.register(request.getUsername(), request.getLogin(), request.getPassword(), request.getEmail());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Author author = authorService.authenticate(request.getLogin(), request.getPassword());
            return ResponseEntity.ok(new LoggedUser(author.getId(), author.getUsername(), author.getLogin(), author.getEmail()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<Boolean> changeEmail(@RequestBody EmailRequest request) {
        try {
            boolean result = authorService.changeEmail(request.getEmail(), request.getId());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/changeUsername")
    public ResponseEntity<Boolean> changeUsername(@RequestBody UsernameRequest request) {
        try {
            boolean result = authorService.changeUsername(request.getUsername(), request.getId());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Boolean> changePassword(@RequestBody PasswordRequest request) {
        try {
            boolean result = authorService.changePassword(request.getPassword(), request.getId());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/changeDescription")
    public ResponseEntity<Boolean> changeDescription(@RequestBody DescRequest request) {
        try {
            boolean result = authorService.changeDesc(request.getDescription(), request.getId());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String login;
        private String password;
        private String email;
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
        private String email;
    }

    @Data
    public static class EmailRequest {
        private String email;
        private long id;
    }

    @Data
    public static class UsernameRequest {
        private String username;
        private long id;
    }

    @Data
    public static class PasswordRequest {
        private String password;
        private long id;
    }

    @Data
    public static class DescRequest {
        private String description;
        private long id;
    }
}
