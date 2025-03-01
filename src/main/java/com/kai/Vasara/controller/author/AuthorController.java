package com.kai.Vasara.controller.author;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.model.author.AuthorDTO;
import com.kai.Vasara.service.author.AuthorService;
import jakarta.validation.Valid;
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

    @GetMapping()
    public ResponseEntity<List<AuthorDTO>> getAuthors() {
        return new ResponseEntity<>(authorService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable Long id) {
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Author author) {
        authorService.register(author);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<?> updateAuthor(@RequestBody Author author) {
        authorService.updateAuthor(author);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
            Author author = authorService.authenticate(request.getLogin(), request.getPassword());
            return ResponseEntity.ok(new LoggedUser(author.getId(), author.getUsername(), author.getLogin(), author.getEmail()));
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
}
