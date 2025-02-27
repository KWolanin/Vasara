package com.kai.Vasara.service;

import com.kai.Vasara.entity.Chapter;
import com.kai.Vasara.entity.FollowingAuthors;
import com.kai.Vasara.entity.FollowingStories;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.repository.FollowingAuthorRepository;
import com.kai.Vasara.repository.FollowingRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@Component
@Slf4j
public class EmailService {

    private final JavaMailSender emailSender;
    private final FollowingRepository followingRepository;
    private final RabbitTemplate rabbitTemplate;
    private final FollowingAuthorRepository followingAuthorRepository;
    private final EnvironmentService environmentService;


    @Autowired
    public EmailService(JavaMailSender emailSender, FollowingRepository followingRepository, RabbitTemplate rabbitTemplate, FollowingAuthorRepository followingAuthorRepository, EnvironmentService environmentService) {
        this.emailSender = emailSender;
        this.followingRepository = followingRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.followingAuthorRepository = followingAuthorRepository;
        this.environmentService = environmentService;
    }

    public void sendMessageAboutNewChapterToQueue(Chapter chapter) {
        ChapterEmailStructure data = prepareChapterMessage(chapter);
        sendMessage(data);
    }

    public void sendMessageAboutNewStoryToQueue(Story story) {
        StoryEmailStructure data = prepareStoryMessage(story);
        sendMessage(data);
    }

    private static StoryEmailStructure prepareStoryMessage(Story story) {
        StoryEmailStructure data = new StoryEmailStructure();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = story.getPublishDt().format(formatter);
        data.setPublished(date);
        data.setStoryId(String.valueOf(story.getId()));
        data.setStoryTitle(story.getTitle());
        data.setUsername(story.getAuthor().getUsername());
        data.setAuthorId(story.getAuthor().getId());
        return data;
    }


    private static ChapterEmailStructure prepareChapterMessage(Chapter chapter) {
        String author = chapter.getStory().getAuthor().getUsername();
        ChapterEmailStructure data = new ChapterEmailStructure();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = chapter.getPublished().format(formatter);
        data.setPublished(date);
        data.setUsername(author);
        data.setStoryId(String.valueOf(chapter.getStory().getId()));
        data.setChapterTitle(chapter.getChapterTitle());
        data.setStoryTitle(chapter.getStory().getTitle());
        data.setChapterNo(String.valueOf(chapter.getChapterNo()));
        return data;
    }

    private void sendMessage(ChapterEmailStructure email) {
        rabbitTemplate.convertAndSend("email-notifications-new-chapter", email);
    }

    private void sendMessage(StoryEmailStructure email) {
        rabbitTemplate.convertAndSend("email-notifications-new-story", email);
    }

    @RabbitListener(queues = "email-notifications-new-chapter")
    private void receiveMessage(ChapterEmailStructure message) {
        sendMailsToStoryFollowers(message);
    }

    @RabbitListener(queues = "email-notifications-new-story")
    private void receiveMessage(StoryEmailStructure message) {
        sendMailsToAuthorFollowers(message);
    }

    private void sendMailsToAuthorFollowers(StoryEmailStructure message) {
        List<FollowingAuthors> authors = followingAuthorRepository.findAllByFollowedAuthor_Id(message.getAuthorId());
        authors.forEach(author -> {
            String email = author.getFollowingAuthor().getEmail();
            String subject = formatTitleForNewStoryEmail(author.getFollowedAuthor().getUsername());
            String body = formatBodyForNewStoryEmail(author.getFollowedAuthor().getUsername(), message.getStoryTitle(), message.getPublished(), message.getStoryId());
            sendEmail(email, subject, body);
        });
    }

    private String formatBodyForNewStoryEmail(String username, String title, String published, String storyId) {
        return String.format("%s published a new story \"%s\" at %s. You can read it %s",
                username, title, published, formatLinkForNewStoryEmail(storyId));
    }

    private Object formatLinkForNewStoryEmail(String storyId) {
        String url = environmentService.isLocalhost() ?
                "http://localhost:9000/#/" : "https://vasaraf-production.up.railway.app/#/";
        return String.format("<a href=\"%sread?storyId=%S&chapterNo=1\">HERE</a>", url, storyId);
    }

    private String formatTitleForNewStoryEmail(String username) {
        return String.format("New story from %s published [Vasara]", username);
    }

    private void sendMailsToStoryFollowers(ChapterEmailStructure message) {
        List<FollowingStories> stories = followingRepository.findByStoryId(Long.parseLong(message.getStoryId()));
        stories.forEach(story -> {
            String email = story.getAuthor().getEmail();
            String subject = formatTitleForNewChapterEmail(message.getStoryTitle(), message.getUsername());
            String body = formatBodyForNewChapterEmail(message.getUsername(), message.getChapterTitle(), message.getStoryTitle(),
                    message.getPublished(), message.getStoryId(), message.getChapterNo());
            sendEmail(email, subject, body);
        });
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            message.setSubject(subject);
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("vasara@app.com");
            helper.setTo(to);
            helper.setText(text, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            log.error("Unable to send email, ", e);
        }
    }


    private String formatTitleForNewChapterEmail(String storyTitle, String username) {
        return String.format("New chapter for %s published by %s [Vasara]", storyTitle, username);
    }

    private String formatBodyForNewChapterEmail(String username, String title, String story, String published, String storyId, String chapterNo) {
        return String.format("%s published a new chapter \"%s\" for story %s at %s. You can read it %s",
                username, title, story, published, formatLinkForNewChapterEmail(storyId, chapterNo));
    }

    private Object formatLinkForNewChapterEmail(String storyId, String chapterNo) {
        String url = environmentService.isLocalhost() ?
                "http://localhost:9000/#/" : "https://vasaraf-production.up.railway.app/#/";
        return String.format("<a href=\"%sread?storyId=%s&chapterNo=%s\">HERE</a>", url, storyId, chapterNo);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ChapterEmailStructure implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private String chapterNo;
        private String storyId;
        private String chapterTitle;
        private String username;
        private String storyTitle;
        private String published;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class StoryEmailStructure implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private String storyId;
        private String username;
        private long authorId;
        private String storyTitle;
        private String published;
    }

}
