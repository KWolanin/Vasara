package com.kai.Vasara.service;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.exception.author.AuthorException;
import com.kai.Vasara.mapper.Mapper;
import com.kai.Vasara.model.author.AuthorInfo;
import com.kai.Vasara.repository.author.AuthorRepository;
import com.kai.Vasara.service.author.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
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

    @Mock private Mapper mapper;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(authorRepository, passwordEncoder, mapper);
    }

    @Test
    void getAuthor_exists() {
        Author author = Author.builder().id(1L).login("login").username("John Doe").stories(Collections.emptyList()).build();
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        AuthorInfo result = authorService.getAuthor(1L);
        assertNotNull(result);
        assertEquals("John Doe", result.getUsername());
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
    void register_userWithExistingLogin_throwsIllegalArgumentException() {
        Author author = Author.builder()
                .username("newuser")
                .login("existinglogin")
                .password("password")
                .email("newuser@example.com")
                        .build();

        when(authorRepository.existsByLogin(author.getLogin())).thenReturn(true);

        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.register(author);
        });

        assertEquals("User with this login already exists", exception.getAuthorError().getMessage());
        verify(authorRepository).existsByLogin(author.getLogin());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void register_userWithExistingUsername_throwsIllegalArgumentException() {
        Author author = Author.builder()
                .username("existingusername")
                .login("newlogin")
                .password("password")
                .email("newuser@example.com")
                .build();
        when(authorRepository.existsByLogin(author.getLogin())).thenReturn(false);
        when(authorRepository.existsByUsername(author.getUsername())).thenReturn(true);

        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.register(author);
        });

        assertEquals("User with this username already exists", exception.getAuthorError().getMessage());
        verify(authorRepository).existsByLogin(author.getLogin());
        verify(authorRepository).existsByUsername(author.getUsername());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void register_userWithExistingEmail_throwsIllegalArgumentException() {
        Author author = Author.builder()
                .username("newuser")
                .login("newlogin")
                .password("password")
                .email("existingemail@example.com")
                .build();

        when(authorRepository.existsByLogin(author.getLogin())).thenReturn(false);
        when(authorRepository.existsByUsername(author.getUsername())).thenReturn(false);
        when(authorRepository.existsByEmail(author.getEmail())).thenReturn(true);

        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.register(author);
        });

        assertEquals("User with this email already exists", exception.getAuthorError().getMessage());
        verify(authorRepository).existsByLogin(author.getLogin());
        verify(authorRepository).existsByUsername(author.getUsername());
        verify(authorRepository).existsByEmail(author.getEmail());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void register_invalidEmail_throwsIllegalArgumentException() {
        Author author = Author.builder()
                .username("newuser")
                .login("newlogin")
                .password("password")
                .email("invalid-email")
                .build();

        when(authorRepository.existsByLogin(author.getLogin())).thenReturn(false);
        when(authorRepository.existsByUsername(author.getUsername())).thenReturn(false);
        when(authorRepository.existsByEmail(author.getEmail())).thenReturn(false);

        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.register(author);
        });

        assertEquals("Email is incorrect", exception.getAuthorError().getMessage());
        verify(authorRepository).existsByLogin(author.getLogin());
        verify(authorRepository).existsByUsername(author.getUsername());
        verify(authorRepository).existsByEmail(author.getEmail());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void register_validData_savesAuthor() {
        Author author = Author.builder()
                .username("newuser")
                .login("newlogin")
                .password("password")
                .email("newuser@example.com")
                .build();

        when(authorRepository.existsByLogin(author.getLogin())).thenReturn(false);
        when(authorRepository.existsByUsername(author.getUsername())).thenReturn(false);
        when(authorRepository.existsByEmail(author.getEmail())).thenReturn(false);

        Author savedAuthor = new Author();
        savedAuthor.setLogin(author.getLogin());
        savedAuthor.setUsername(author.getUsername());
        savedAuthor.setEmail(author.getEmail());
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        Author result = authorService.register(author);

        assertNotNull(result);
        assertEquals(author.getLogin(), result.getLogin());
        assertEquals(author.getUsername(), result.getUsername());
        assertEquals(author.getEmail(), result.getEmail());
        verify(authorRepository).existsByLogin(author.getLogin());
        verify(authorRepository).existsByUsername(author.getUsername());
        verify(authorRepository).existsByEmail(author.getEmail());
        verify(authorRepository).save(any(Author.class));
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void authenticate_userNotFound_throwsIllegalArgumentException() {
        String login = "nonexistentlogin";
        String rawPassword = "password";

        when(authorRepository.findByLogin(login)).thenReturn(Optional.empty());

        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.authenticate(login, rawPassword);
        });

        assertEquals("User not found", exception.getAuthorError().getMessage());
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

        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.authenticate(login, rawPassword);
        });

        assertEquals("Invalid login or password", exception.getAuthorError().getMessage());
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
    void updateAuthor_throws_not_found() {
        Author newAuthorData = new Author();


        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.updateAuthor(newAuthorData);
        });

        assertEquals("User not found", exception.getAuthorError().getMessage());
    }

    @Test
     void updateAuthor() {
        Author newAuthorData = Author.builder()
                .email("newemail@mail.com")
                .username("newusername")
                .password("newpassword")
                .description("newdescription")
                .build();

        Author oldAuthorData = Author.builder()
                .email("oldemail@mail.com")
                .username("oldusername")
                .password("oldpassword")
                .description("olddescription")
                .build();

        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.ofNullable(oldAuthorData));
        when(passwordEncoder.encode("newpassword")).thenReturn("newpassword");
        authorService.updateAuthor(newAuthorData);

        assertEquals("newusername", oldAuthorData.getUsername());
        assertEquals("newpassword", oldAuthorData.getPassword());
        assertEquals("newemail@mail.com", oldAuthorData.getEmail());
        assertEquals("newdescription", oldAuthorData.getDescription());
        verify(authorRepository).save(oldAuthorData);
    }

    @Test
    void updateAuthor_throws_email_invalid() {
        Author newAuthorData = Author.builder()
                .email("invalid_email")
                .username("newusername")
                .password("newpassword")
                .description("newdescription")
                .build();

        Author oldAuthorData = Author.builder()
                .email("oldemail@mail.com")
                .username("oldusername")
                .password("oldpassword")
                .description("olddescription")
                .build();

        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.ofNullable(oldAuthorData));


        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.updateAuthor(newAuthorData);
        });

        assertEquals("Email is invalid", exception.getAuthorError().getMessage());
    }

    @Test
    void updateAuthor_throws_email_is_taken_by_another_user() {
        Author newAuthorData = Author.builder()
                .email("emailtaken@byanother.com")
                .username("newusername")
                .password("newpassword")
                .description("newdescription")
                .build();

        Author oldAuthorData = Author.builder()
                .email("oldemail@mail.com")
                .username("oldusername")
                .password("oldpassword")
                .description("olddescription")
                .build();

        when(authorRepository.findById(any())).thenReturn(Optional.ofNullable(oldAuthorData));
        when(authorRepository.existsByEmail(any())).thenReturn(true);


        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.updateAuthor(newAuthorData);
        });

        assertEquals("User with this email already exists", exception.getAuthorError().getMessage());
    }

    @Test
    void updateAuthor_throws_username_is_taken_by_another_user() {
        Author newAuthorData = Author.builder()
                .email("emailtaken@byanother.com")
                .username("newusername")
                .password("newpassword")
                .description("newdescription")
                .build();

        Author oldAuthorData = Author.builder()
                .email("oldemail@mail.com")
                .username("oldusername")
                .password("oldpassword")
                .description("olddescription")
                .build();

        when(authorRepository.findById(any())).thenReturn(Optional.ofNullable(oldAuthorData));
        when(authorRepository.existsByUsername(any())).thenReturn(true);


        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.updateAuthor(newAuthorData);
        });

        assertEquals("User with this username already exists", exception.getAuthorError().getMessage());
    }

    @Test
    void updateAuthor_throws_password_empty() {
        Author newAuthorData = Author.builder()
                .email("emailtaken@byanother.com")
                .username("newusername")
                .password("")
                .description("newdescription")
                .build();

        Author oldAuthorData = Author.builder()
                .email("oldemail@mail.com")
                .username("oldusername")
                .password("oldpassword")
                .description("olddescription")
                .build();

        when(authorRepository.findById(any())).thenReturn(Optional.ofNullable(oldAuthorData));


        AuthorException exception = assertThrows(AuthorException.class, () -> {
            authorService.updateAuthor(newAuthorData);
        });

        assertEquals("Password is invalid", exception.getAuthorError().getMessage());
    }



}