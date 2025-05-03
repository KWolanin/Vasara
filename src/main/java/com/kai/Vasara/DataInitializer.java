package com.kai.Vasara;


import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.model.StoryRating;
import com.kai.Vasara.repository.author.AuthorRepository;
import com.kai.Vasara.repository.chapter.ChapterRepository;
import com.kai.Vasara.repository.story.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
/*
    Loads example data for database;
 */
public class DataInitializer implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final StoryRepository storyRepository;
    private final ChapterRepository chapterRepository;

    @Override
    public void run(String... args) {
        if (authorRepository.count() == 0) {
            Author author = Author.builder()
                    .login("admin")
                    .password("admin")
                    .username("Admin Author")
                    .email("admin@admin.com")
                    .build();

            authorRepository.save(author);

            Story story = Story.builder()
                    .author(author)
                    .title("Lorem lorem!")
                    .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...")
                    .finished(false)
                    .publishDt(ZonedDateTime.now())
                    .updateDt(ZonedDateTime.now())
                    .rating(StoryRating.KIDS)
                    .allowComments(true)
                    .allowGuestComments(true)
                    .build();

            storyRepository.save(story);

            Chapter chapter = new Chapter();
            chapter.setChapterNo(1);
            chapter.setChapterTitle("Adventure started!");
            chapter.setContent("<p>Lorem ipsum dolor sit amet, <strong>consectetur</strong> adipiscing elit.</p>");
            chapter.setPublished(ZonedDateTime.now());
            chapter.setUpdated(ZonedDateTime.now());
            chapter.setStory(story);

            chapterRepository.save(chapter);
        }
    }
}
