package com.kai.Vasara.service;

import com.kai.Vasara.entity.Story;
import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class StoryService {

    private final StoryRepository storyRepository;
    private final AuthorService authorService;
    private final ChapterService chapterService;

    @Autowired
    public StoryService(StoryRepository storyRepository, AuthorService authorService,
                        ChapterService chapterService) {
        this.storyRepository = storyRepository;
        this.authorService = authorService;
        this.chapterService = chapterService;
    }

    public List<StoryDAO> getAll() {
        List<Story> stories = storyRepository.findAll();
        List<StoryDAO> daos = new ArrayList<>();
        stories.forEach(story -> {
            daos.add(from(story));
        });
        return daos;
    }

    public StoryDAO getStory(Long id) {
        Optional<Story> opt = storyRepository.findById(id);
        return opt.map(this::from).orElse(null);
    }

    public Boolean saveStory(StoryDAO story) {
        try {
            storyRepository.save(from(story));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public List<StoryDAO> getMyStories(Long id) {
        List<Story> stories = storyRepository.findAllByAuthorId(id);
        List<StoryDAO> daos = new ArrayList<>();
        stories.forEach(story -> daos.add(from(story)));
        return daos;
    }

    public StoryDAO from(Story story) {
        StoryDAO s = new StoryDAO();
        s.setId(story.getId());
        s.setAuthorName(authorService.getAuthorName(story.getAuthorId()));
        s.setDescription(story.getDescription());
        s.setTitle(story.getTitle());
        s.setTags(splitAndRemoveQuotes(story.getTags()));
        s.setFandoms(splitAndRemoveQuotes(story.getFandoms()));
        s.setFinished(story.isFinished());
        s.setPublishDt(story.getPublishDt());
        s.setUpdateDt(story.getUpdateDt());
        s.setChaptersNumber(chapterService.getChapterNumber(story.getId()));
        return s;
    }

    public Story from(StoryDAO storyDAO) {
        Story story = new Story();
        story.setAuthorId(storyDAO.getAuthorId());
        story.setDescription(storyDAO.getDescription());
        story.setTitle(storyDAO.getTitle());
        story.setTags(String.join(",", storyDAO.getTags()));
        story.setFandoms(String.join(",", storyDAO.getFandoms()));
        story.setFinished(storyDAO.isFinished());
        story.setPublishDt(storyDAO.getPublishDt());
        story.setUpdateDt(storyDAO.getUpdateDt());
        return story;
    }

    public List<StoryDAO> getStoriesByAuthor(Long id) {
        List<StoryDAO> daos = new ArrayList<>();
        List<Story> stories = storyRepository.findAllByAuthorId(id);
        stories.forEach(s -> {
            daos.add(from(s));
        });
        return daos;
    }

    public static List<String> splitAndRemoveQuotes(String input) {
        List<String> resultList = new ArrayList<>();
        if (StringUtils.hasLength(input)) {
            input = input.substring(1, input.length() - 1);
            String[] parts = input.split(",");
            for (String part : parts) {
                resultList.add(part.replace("\"", "").trim());
            }
        }
        return resultList;
    }

    @Transactional
    public Boolean deleteStory(Long id) {
        return chapterService.deleteChaptersForStory(id) && storyRepository.deleteStoryById(id) > 0;
    }
}
