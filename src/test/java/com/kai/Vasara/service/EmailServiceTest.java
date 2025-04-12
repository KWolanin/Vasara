package com.kai.Vasara.service;

import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.author.FollowAuthor;
import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.entity.story.FollowStory;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.repository.author.FollowAuthorRepository;
import com.kai.Vasara.repository.story.FollowStoryRepository;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private FollowStoryRepository followStoryRepository;

    @Mock
    private FollowAuthorRepository followAuthorRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendMessageAboutNewChapterToQueue_sendsMessageToQueue() {
        Chapter chapter = new Chapter();
        chapter.setChapterNo(1);
        chapter.setChapterTitle("Chapter 1");
        chapter.setPublished(ZonedDateTime.now());
        Story story = new Story();
        story.setId(1L);
        story.setTitle("Story Title");
        Author author = new Author();
        author.setUsername("AuthorName");
        story.setAuthor(author);
        chapter.setStory(story);

        emailService.sendMessageAboutNewChapterToQueue(chapter);

        verify(rabbitTemplate).convertAndSend(
                eq("email-notifications-new-chapter"),
                any(EmailService.ChapterEmailStructure.class)
        );
    }

    @Test
    void sendMessageAboutNewStoryToQueue_sendsMessageToQueue() {
        Story story = new Story();
        story.setId(1L);
        story.setTitle("Story Title");
        story.setPublishDt(ZonedDateTime.now());
        Author author = new Author();
        author.setUsername("AuthorName");
        author.setId(1L);
        story.setAuthor(author);

        emailService.sendMessageAboutNewStoryToQueue(story);

        verify(rabbitTemplate).convertAndSend(eq("email-notifications-new-story")
                , any(EmailService.StoryEmailStructure.class));
    }

    @Test
    void sendMailsToAuthorFollowers_sendsEmailsToAllFollowers() {
        EmailService.StoryEmailStructure message = new EmailService.StoryEmailStructure();
        message.setAuthorId(1L);
        message.setStoryTitle("Story Title");
        message.setPublished("01/01/2023");
        message.setStoryId("1");
        message.setUsername("AuthorName");


        FollowAuthor followAuthor = new FollowAuthor();
        Author followingAuthor = new Author();
        followingAuthor.setEmail("test@example.com");
        followAuthor.setFollowingAuthor(followingAuthor);
        Author followedAuthor = new Author();
        followedAuthor.setUsername("AuthorName");
        followAuthor.setFollowedAuthor(followedAuthor);

        when(followAuthorRepository.findAllByFollowedAuthor_Id(1L)).thenReturn(List.of(followAuthor));
        when(environmentService.isLocalhost()).thenReturn(true);
        when(emailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        emailService.sendMailsToAuthorFollowers(message);

        verify(emailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendMailsToStoryFollowers_sendsEmailsToAllFollowers() {
        EmailService.ChapterEmailStructure message = new EmailService.ChapterEmailStructure();
        message.setStoryId("1");
        message.setStoryTitle("Story Title");
        message.setChapterTitle("Chapter 1");
        message.setPublished("01/01/2023");

        FollowStory followStory = new FollowStory();
        Author author = new Author();
        author.setEmail("test@example.com");
        followStory.setAuthor(author);

        when(followStoryRepository.findByStoryId(1L)).thenReturn(List.of(followStory));
        when(environmentService.isLocalhost()).thenReturn(true);
        when(emailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        emailService.sendMailsToStoryFollowers(message);

        verify(emailSender).send(any(MimeMessage.class));
    }
}