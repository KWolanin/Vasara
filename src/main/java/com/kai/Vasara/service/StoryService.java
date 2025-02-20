package com.kai.Vasara.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.Vasara.entity.Author;
import com.kai.Vasara.entity.Story;
import com.kai.Vasara.model.AuthorDAO;
import com.kai.Vasara.model.Criteria;
import com.kai.Vasara.model.StoryDAO;
import com.kai.Vasara.repository.StoryRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Component
@Slf4j
public class StoryService {

    private final StoryRepository storyRepository;
    private final AuthorService authorService;
    private final ChapterService chapterService;
    private final FavoriteService favoriteService;
    private final FollowingService followingService;
    private final ReadService readService;
    private static final Logger logger = LoggerFactory.getLogger(StoryService.class);

    @Autowired
    public StoryService(StoryRepository storyRepository, AuthorService authorService,
                        ChapterService chapterService, @Lazy FavoriteService favoriteService,
                        @Lazy FollowingService followingService, @Lazy ReadService readService) {
        this.storyRepository = storyRepository;
        this.authorService = authorService;
        this.chapterService = chapterService;
        this.favoriteService = favoriteService;
        this.followingService = followingService;
        this.readService = readService;
    }

    public List<StoryDAO> getAll() {
        List<Story> stories = storyRepository.findAll();
        List<StoryDAO> daos = new ArrayList<>();
        stories.forEach(story -> daos.add(from(story)));
        return daos;
    }

    @Cacheable(value = "storiesCache", key = "{#page, #size, #criteria.hashCode(), #sortBy}")
    public Page<StoryDAO> getPage(int page, int size, Criteria criteria, String sortBy) {
        return criteria.areFieldsEmpty() ?  getPageWithoutCriteria(page, size, sortBy)
            : getPageWithCriteria(page, size, criteria, sortBy);
    }

    private PageImpl<StoryDAO> getPageWithoutCriteria(int page, int size, String sortBy) {
        Sort sort = getSortType(sortBy);
        Pageable pageable = PageRequest.of(page -1, size, sort);
        Page<Story> storiesPage = storyRepository.findAllWithChapters(pageable);
        List<StoryDAO> daos = storiesPage.getContent().stream()
                .map(this::from)
                .collect(Collectors.toList());
        return new PageImpl<>(daos, pageable, storiesPage.getTotalElements());
    }

    private PageImpl<StoryDAO> getPageWithCriteria(int page, int size, Criteria criteria, String sortBy) {
        Sort sort = getSortType(sortBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Specification<Story> spec = buildSpecification(criteria);
        Page<Story> storiesPage = storyRepository.findAll(spec, pageable);
        List<StoryDAO> daos = storiesPage.getContent().stream()
                .map(this::from)
                .collect(Collectors.toList());
        return new PageImpl<>(daos, pageable, storiesPage.getTotalElements());
    }

    private static Sort getSortType(String sortBy) {
        Sort sort = null;
        if (sortBy != null) {
            switch (sortBy) {
                case "updateDt":
                    sort = Sort.by(Sort.Order.desc("updateDt"));
                    break;
                case "updateDt asc":
                    sort = Sort.by(Sort.Order.asc("updateDt"));
                    break;
                case "author":
                    sort = Sort.by(Sort.Order.asc("author.username"));
                    break;
                case "author desc":
                    sort = Sort.by(Sort.Order.desc("author.username"));
                    break;
                case "title":
                    sort = Sort.by(Sort.Order.asc("title"));
                    break;
                case "title desc":
                    sort = Sort.by(Sort.Order.desc("title"));
                    break;
                default:
                    break;
            }
        }
        return sort;
    }

    private Specification<Story> buildSpecification(Criteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getTitle() != null && !criteria.getTitle().trim().isEmpty()) {
                predicates.add(builder.like(
                        builder.lower(root.get("title")), "%" + criteria.getTitle().toLowerCase() + "%"
                ));
            }
            if (criteria.getAuthor() != null && !criteria.getAuthor().trim().isEmpty()) {
                predicates.add(builder.like(
                        builder.lower(root.get("author").get("username")), "%" + criteria.getAuthor().toLowerCase() + "%"
                ));
            }
            if (criteria.getDescription() != null && !criteria.getDescription().trim().isEmpty()) {
                predicates.add(builder.like(
                        builder.lower(root.get("description")), "%" + criteria.getDescription().toLowerCase() + "%"
                ));
            }
            if (criteria.getFandoms() != null && !criteria.getFandoms().isEmpty()) {
                for (String fandom : criteria.getFandoms()) {
                    predicates.add(builder.like(
                            builder.lower(root.get("fandoms")), "%" + fandom.toLowerCase() + "%"
                    ));
                }
            }
            if (criteria.getTags() != null && !criteria.getTags().isEmpty()) {
                for (String tag : criteria.getTags()) {
                    predicates.add(builder.like(
                            builder.lower(root.get("tags")), "%" + tag.toLowerCase() + "%"
                    ));
                }
            }
            predicates.add(builder.isNotEmpty(root.get("chapters")));
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }


    @Cacheable(value = "userStoriesCache", key = "{#id, #page, #size}")
    public Page<StoryDAO> getMyStories(Long id, int page, int size) {
        Sort sort = Sort.by(Sort.Order.desc("updateDt"));
        Pageable pageable = PageRequest.of(page -1, size, sort);
        Page<Story> stories = storyRepository.findAllByAuthorId(id, pageable);
        List<StoryDAO> daos = new ArrayList<>();
        stories.forEach(story -> daos.add(from(story)));
        return  new PageImpl<>(daos, pageable, stories.getTotalElements());
    }


    public StoryDAO getStory(Long id) {
        Optional<Story> opt = storyRepository.findById(id);
        return opt.map(this::from).orElse(null);
    }

    @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true)
    public Boolean saveStory(StoryDAO story) {
        try {
            storyRepository.save(from(story));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public StoryDAO from(Story story) {
        StoryDAO s = new StoryDAO();
        s.setId(story.getId());
        s.setAuthorName(story.getAuthor().getUsername());
        s.setAuthorId(story.getAuthor().getId());
        s.setDescription(story.getDescription());
        s.setTitle(story.getTitle());
        s.setTags(splitAndRemoveQuotes(story.getTags()));
        s.setFandoms(splitAndRemoveQuotes(story.getFandoms()));
        s.setFinished(story.isFinished());
        s.setPublishDt(story.getPublishDt());
        s.setUpdateDt(story.getUpdateDt());
        s.setChaptersNumber(chapterService.getChapterNumber(story.getId()));

        if (story.getAuthor() != null) {
            AuthorDAO authorDAO = new AuthorDAO();
            authorDAO.setUsername(story.getAuthor().getUsername());
            authorDAO.setId(story.getAuthor().getId());
        }

        return s;
    }

    public Story from(StoryDAO storyDAO) {
        Story story = new Story();
        story.setDescription(storyDAO.getDescription());
        story.setTitle(storyDAO.getTitle());
        updateStoryTagsAndFandoms(story, storyDAO);
        story.setFinished(storyDAO.isFinished());
        story.setPublishDt(storyDAO.getPublishDt());
        story.setUpdateDt(storyDAO.getUpdateDt());

        if (storyDAO.getAuthorId() > 0) {
            Optional<Author> author = authorService.find(storyDAO.getAuthorId());
            author.ifPresent(story::setAuthor);
        }

        return story;
    }

    public void updateStoryTagsAndFandoms(Story story, StoryDAO storyDAO) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (Objects.nonNull(storyDAO.getTags())) {
                String tagsJson = objectMapper.writeValueAsString(storyDAO.getTags());
                story.setTags(tagsJson);
            } else story.setTags("[\"\"]");
            if (Objects.nonNull(storyDAO.getFandoms())) {
                String fandomsJson = objectMapper.writeValueAsString(storyDAO.getFandoms());
                story.setFandoms(fandomsJson);
            } else story.setFandoms("[\"\"]");
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new RuntimeException("Error serializing to JSON", e);
        }
    }

    public static List<String> splitAndRemoveQuotes(String input) {
        List<String> resultList = new ArrayList<>();
        if (StringUtils.hasLength(input) && !noTag(input)) {
            input = input.substring(1, input.length() - 1);
            String[] parts = input.split(",");
            for (String part : parts) {
                resultList.add(part.replace("\"", "").trim());
            }
        }
        return resultList;
    }

    private static boolean noTag(String tag) {
        return "[]".equals(tag) || tag.isEmpty();
    }

    public static String joinAndAddQuotes(List<String> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            return "[]";
        }
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < inputList.size(); i++) {
            result.append("\"").append(inputList.get(i)).append("\"");
            if (i < inputList.size() - 1) {
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
    }


    @Transactional
    public Boolean deleteStory(Long id) {
        chapterService.deleteChaptersForStory(id);
        favoriteService.delete(id);
        followingService.delete(id);
        readService.delete(id);
        int storiesDeleted = storyRepository.deleteStoryById(id);
        return storiesDeleted > 0;
    }

    public Boolean editStory(StoryDAO storyDAO) {
        Optional<Story> story = storyRepository.findById(storyDAO.getId());
        if (story.isPresent()) {
            Story s = story.get();
            s.setTitle(storyDAO.getTitle());
            s.setDescription(storyDAO.getDescription());
            s.setFandoms(joinAndAddQuotes(storyDAO.getFandoms()));
            s.setFinished(storyDAO.isFinished());
            s.setTags(joinAndAddQuotes(storyDAO.getTags()));
             storyRepository.save(s);
             return true;
        }
        return false;
    }

    public long count() {
        return storyRepository.count();
    }

    public long countMine(Long id) {
        return storyRepository.countMine(id);
    }

    public Optional<Story> findById(long id) {
        return storyRepository.findById(id);
    }

}
