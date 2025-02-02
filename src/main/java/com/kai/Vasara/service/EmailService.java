package com.kai.Vasara.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.Vasara.RabbitMQ.RabbitMQProducer;
import com.kai.Vasara.entity.Chapter;
import com.kai.Vasara.entity.FollowingStories;
import com.kai.Vasara.repository.FollowingRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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

    private final RabbitMQProducer mqProducer;
    private final JavaMailSender emailSender;
    private final FollowingRepository followingRepository;

    @Autowired
    public EmailService(RabbitMQProducer mqProducer, JavaMailSender emailSender, FollowingRepository followingRepository) {
        this.mqProducer = mqProducer;
        this.emailSender = emailSender;
        this.followingRepository = followingRepository;
    }

    public void sendMessageToQueue(Chapter chapter) {
        String author = chapter.getStory().getAuthor().getUsername();
        DataStructure data = new DataStructure();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = chapter.getPublished().format(formatter);
        data.setPublished(date);
        data.setUsername(author);
        data.setStoryId(String.valueOf(chapter.getStory().getId()));
        data.setChapterTitle(chapter.getChapterTitle());
        data.setStoryTitle(chapter.getStory().getTitle());
        data.setChapterNo(String.valueOf(chapter.getChapterNo()));
        mqProducer.sendMessage(data);
    }

    private String formatTitle(String storyTitle, String username) {
        return String.format("New chapter for %s published by %s [Vasara]", storyTitle, username);
    }

    private String formatBody(String username, String title, String story, String published, String storyId, String chapterNo) {
        return String.format("%s published a new chapter \"%s\" for story %s at %s. You can read it %s",
                username, title, story, published, formatLink(storyId, chapterNo));
    }

    private Object formatLink(String storyId, String chapterNo) {
        return String.format("<a href=\"https://vasaraf-production.up.railway.app/#/read?storyId=%s&chapterNo=%s\">HERE</a>", storyId, chapterNo);
    }

    public void sendMailsToFollowers(String message) {
        DataStructure data;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            data = objectMapper.readValue(message, DataStructure.class);
        } catch (Exception e) {
            log.warn("Unable to read jso and send an email");
            return;
        }
        List<FollowingStories> stories = followingRepository.findByStoryId(Long.parseLong(data.getStoryId()));
        DataStructure finalData = data;
        stories.forEach(story -> {
            String email = story.getAuthor().getEmail();
            String subject = formatTitle(finalData.getStoryTitle(), finalData.getUsername());
            String body = formatBody(finalData.getUsername(), finalData.getChapterTitle(), finalData.getStoryTitle(),
                    finalData.getPublished(), finalData.getStoryId(), finalData.getChapterNo());
            sendEmail(email, subject, body);
        });
    }

    public void sendEmail(String to, String subject, String text) {
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

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DataStructure implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private String chapterNo;
        private String storyId;
        private String chapterTitle;
        private String username;
        private String storyTitle;
        private String published;
    }
}
