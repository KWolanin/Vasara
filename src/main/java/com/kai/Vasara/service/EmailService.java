package com.kai.Vasara.service;

import com.kai.Vasara.entity.Chapter;
import com.kai.Vasara.entity.FollowingStories;
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


    @Autowired
    public EmailService(JavaMailSender emailSender, FollowingRepository followingRepository, RabbitTemplate rabbitTemplate) {
        this.emailSender = emailSender;
        this.followingRepository = followingRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessageToQueue(Chapter chapter) {
        DataStructure data = prepareMessage(chapter);
        sendMessage(data);
    }

    private static DataStructure prepareMessage(Chapter chapter) {
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
        return data;
    }

    public void sendMessage(EmailService.DataStructure email) {
        rabbitTemplate.convertAndSend("email-notifications", email);
    }

    @RabbitListener(queues = "email-notifications")
    public void receiveMessage(EmailService.DataStructure message) {
        sendMailsToFollowers(message);
    }

    private void sendMailsToFollowers(DataStructure message) {
        List<FollowingStories> stories = followingRepository.findByStoryId(Long.parseLong(message.getStoryId()));
        stories.forEach(story -> {
            String email = story.getAuthor().getEmail();
            String subject = formatTitle(message.getStoryTitle(), message.getUsername());
            String body = formatBody(message.getUsername(), message.getChapterTitle(), message.getStoryTitle(),
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
