package com.kai.Vasara.service;

import com.kai.Vasara.entity.Author;
import com.kai.Vasara.model.AuthorDAO;
import com.kai.Vasara.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {


    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Mock
    private  PasswordEncoder passwordEncoder;


    @Test
    void getAll_return_all_authors() {
        List<Author> authors = List.of(
                Author.builder().id(1L).username("John Doe").stories(Collections.emptyList()).build(),
                Author.builder().id(2L).username("Jane Smith").stories(Collections.emptyList()).build()
        );
        when(authorRepository.findAll()).thenReturn(authors);
        List<AuthorDAO> result = authorService.getAll();
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getUsername());
        assertEquals("Jane Smith", result.get(1).getUsername());
        verify(authorRepository).findAll();
    }

    @Test
    void getAll_return_empty_list() {
        when(authorRepository.findAll()).thenReturn(Collections.emptyList());
        List<AuthorDAO> result = authorService.getAll();
        assertTrue(result.isEmpty());
        verify(authorRepository).findAll();
    }

    @Test
    void getAuthor_exists() {
        Author author = Author.builder().id(1L).login("login").username("John Doe").stories(Collections.emptyList()).build();
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        AuthorDAO result = authorService.getAuthor(1L);
        assertNotNull(result);
        assertEquals("John Doe", result.getUsername());
    }

    @Test
    void getAuthor_not_exists() {
        when(authorRepository.findById(2L)).thenReturn(Optional.empty());
        AuthorDAO result = authorService.getAuthor(2L);
        assertNull(result);
    }

    @Test
    void saveAuthor_success() {
        Author author = Author.builder().id(1L).login("login").username("username").build();
        when(authorRepository.save(author)).thenReturn(author);
        Boolean result = authorService.saveAuthor(author);
        assertTrue(result);
        verify(authorRepository).save(author);
    }

    @Test
    void saveAuthor_failed() {
        Author author = Author.builder().id(1L).login("login").username("username").build();
        when(authorRepository.save(author)).thenThrow(new RuntimeException("Database error"));
        Boolean result = authorService.saveAuthor(author);
        assertFalse(result);
        verify(authorRepository).save(author);
    }


    @Test
    void getAuthorName_authorFound_returnsUsername() {
        Long authorId = 1L;
        String expectedUsername = "John Doe";
        when(authorRepository.findUsernameById(authorId)).thenReturn(Optional.of(expectedUsername));
        String result = authorService.getAuthorName(authorId);
        assertEquals(expectedUsername, result);
        verify(authorRepository).findUsernameById(authorId);
    }

    @Test
    void getAuthorDesc_authorFound_returnsDesc() {
        Long authorId = 1L;
        String desc = "Hello, I am X.";
        when(authorRepository.findDescriptionById(authorId)).thenReturn(Optional.of(desc));
        String result = authorService.getAuthorDesc(authorId);
        assertEquals(desc, result);
    }

    @Test
    void getAuthorName_authorNotFound_returnsEmptyString() {
        Long authorId = 2L;
        when(authorRepository.findUsernameById(authorId)).thenReturn(Optional.empty());
        String result = authorService.getAuthorName(authorId);
        assertEquals("", result);
        verify(authorRepository).findUsernameById(authorId);
    }


    @Test
    void from_authorToAuthorDAO_returnsCorrectAuthorDAO() {
        Author author = Author.builder().id(1L).login("login").stories(Collections.emptyList()).username("username").build();
        AuthorDAO result = authorService.from(author);
        assertNotNull(result);
        assertEquals(author.getId(), result.getId());
        assertEquals(author.getUsername(), result.getUsername());
        assertEquals(author.getEmail(), result.getEmail());
    }

    @Test
    void from_authorDAOToAuthor_returnsCorrectAuthor() {
        AuthorDAO authorDAO = AuthorDAO.builder().id(1L).username("username").email("e@e.com").stories(Collections.emptyList()).build();
        Author result = authorService.from(authorDAO);
        assertNotNull(result);
        assertEquals(authorDAO.getId(), result.getId());
        assertEquals(authorDAO.getUsername(), result.getUsername());
        assertEquals(authorDAO.getEmail(), result.getEmail());
    }

    @Test
    void register_userWithExistingLogin_throwsIllegalArgumentException() {
        String username = "newuser";
        String login = "existinglogin";
        String rawPassword = "password";
        String email = "newuser@example.com";
        when(authorRepository.findByLogin(login)).thenReturn(Optional.of(new Author()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.register(username, login, rawPassword, email);
        });

        assertEquals("User with this login already exists", exception.getMessage());
        verify(authorRepository).findByLogin(login);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void register_userWithExistingUsername_throwsIllegalArgumentException() {
        String username = "existingusername";
        String login = "newlogin";
        String rawPassword = "password";
        String email = "newuser@example.com";
        when(authorRepository.findByLogin(login)).thenReturn(Optional.empty());
        when(authorRepository.findByUsername(username)).thenReturn(Optional.of(new Author()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.register(username, login, rawPassword, email);
        });

        assertEquals("User with this username already exists", exception.getMessage());
        verify(authorRepository).findByLogin(login);
        verify(authorRepository).findByUsername(username);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void register_userWithExistingEmail_throwsIllegalArgumentException() {
        String username = "newuser";
        String login = "newlogin";
        String rawPassword = "password";
        String email = "existingemail@example.com";
        when(authorRepository.findByLogin(login)).thenReturn(Optional.empty());
        when(authorRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(authorRepository.findByEmail(email)).thenReturn(Optional.of(new Author()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.register(username, login, rawPassword, email);
        });

        assertEquals("User with this email already exists", exception.getMessage());
        verify(authorRepository).findByLogin(login);
        verify(authorRepository).findByUsername(username);
        verify(authorRepository).findByEmail(email);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void register_invalidEmail_throwsIllegalArgumentException() {
        String username = "newuser";
        String login = "newlogin";
        String rawPassword = "password";
        String email = "invalid-email";
        when(authorRepository.findByLogin(login)).thenReturn(Optional.empty());
        when(authorRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(authorRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.register(username, login, rawPassword, email);
        });

        assertEquals("Email is incorrect", exception.getMessage());
        verify(authorRepository).findByLogin(login);
        verify(authorRepository).findByUsername(username);
        verify(authorRepository).findByEmail(email);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void register_validData_savesAuthor() {
        String username = "newuser";
        String login = "newlogin";
        String rawPassword = "password";
        String email = "newuser@example.com";
        when(authorRepository.findByLogin(login)).thenReturn(Optional.empty());
        when(authorRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(authorRepository.findByEmail(email)).thenReturn(Optional.empty());

        Author savedAuthor = new Author();
        savedAuthor.setLogin(login);
        savedAuthor.setUsername(username);
        savedAuthor.setEmail(email);
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        Author result = authorService.register(username, login, rawPassword, email);

        assertNotNull(result);
        assertEquals(login, result.getLogin());
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        verify(authorRepository).findByLogin(login);
        verify(authorRepository).findByUsername(username);
        verify(authorRepository).findByEmail(email);
        verify(authorRepository).save(any(Author.class));
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void authenticate_userNotFound_throwsIllegalArgumentException() {
        String login = "nonexistentlogin";
        String rawPassword = "password";

        when(authorRepository.findByLogin(login)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.authenticate(login, rawPassword);
        });

        assertEquals("User not found", exception.getMessage());
        verify(authorRepository).findByLogin(login);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void authenticate_invalidPassword_throwsIllegalArgumentException() {
        String login = "existinglogin";
        String rawPassword = "wrongpassword";
        String storedPassword = "encodedpassword";
        Author author = new Author();
        author.setLogin(login);
        author.setPassword(storedPassword);

        when(authorRepository.findByLogin(login)).thenReturn(Optional.of(author));
        when(passwordEncoder.matches(rawPassword, storedPassword)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.authenticate(login, rawPassword);
        });

        assertEquals("Invalid login or password", exception.getMessage());
        verify(authorRepository).findByLogin(login);
        verify(passwordEncoder).matches(rawPassword, storedPassword);
        verifyNoMoreInteractions(authorRepository, passwordEncoder);
    }

    @Test
    void authenticate_validCredentials_returnsAuthor() {
        String login = "existinglogin";
        String rawPassword = "correctpassword";
        String storedPassword = "encodedpassword";
        Author author = new Author();
        author.setLogin(login);
        author.setPassword(storedPassword);

        when(authorRepository.findByLogin(login)).thenReturn(Optional.of(author));
        when(passwordEncoder.matches(rawPassword, storedPassword)).thenReturn(true);

        Author result = authorService.authenticate(login, rawPassword);

        assertNotNull(result);
        assertEquals(login, result.getLogin());
        verify(authorRepository).findByLogin(login);
        verify(passwordEncoder).matches(rawPassword, storedPassword);
        verifyNoMoreInteractions(authorRepository, passwordEncoder);
    }

    @Test
    void find_authorExists_returnsAuthor() {
        long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);
        author.setLogin("existinglogin");
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        Optional<Author> result = authorService.find(authorId);

        assertTrue(result.isPresent());
        assertEquals(authorId, result.get().getId());
        assertEquals("existinglogin", result.get().getLogin());
        verify(authorRepository).findById(authorId);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void find_authorNotFound_returnsEmptyOptional() {
        long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());
        Optional<Author> result = authorService.find(authorId);

        assertFalse(result.isPresent());
        verify(authorRepository).findById(authorId);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void findById_success() {
        Author author = new Author();
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        Optional<Author> a = authorService.findById(1L);
        assertTrue(a.isPresent());
        assertSame(a.get(), author);
    }

    @Test
    void findById_none() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Author> a = authorService.findById(1L);
        assertFalse(a.isPresent());
    }

}