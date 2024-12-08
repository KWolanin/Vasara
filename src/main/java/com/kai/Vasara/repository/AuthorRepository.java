package com.kai.Vasara.repository;

import com.kai.Vasara.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a.username FROM Author a WHERE a.id = :id")
    Optional<String> findUsernameById(@Param("id") Long id);

    Optional<Author> findAutorByUsername(String username);
}
