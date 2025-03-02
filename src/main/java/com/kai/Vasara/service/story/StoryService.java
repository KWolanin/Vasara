package com.kai.Vasara.service.story;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kai.Vasara.entity.author.Author;
import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.exception.story.StoryError;
import com.kai.Vasara.exception.story.StoryException;
import com.kai.Vasara.model.author.AuthorDTO;
import com.kai.Vasara.model.SearchCriteria;
import com.kai.Vasara.model.story.StoryDTO;
import com.kai.Vasara.repository.story.StoryRepository;
import com.kai.Vasara.service.author.AuthorService;
import com.kai.Vasara.service.chapter.ChapterService;
import com.kai.Vasara.service.comment.CommentService;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
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
    private final FavoriteServiceStory favoriteService;
    private final FollowServiceStory followingService;
    private final ReadLaterService readService;
    private final CommentService commentService;

    @Autowired
    public StoryService(StoryRepository storyRepository, AuthorService authorService,
                        ChapterService chapterService, @Lazy FavoriteServiceStory favoriteService,
                        @Lazy FollowServiceStory followingService, @Lazy ReadLaterService readService, CommentService commentService) {
        this.storyRepository = storyRepository;
        this.authorService = authorService;
        this.chapterService = chapterService;
        this.favoriteService = favoriteService;
        this.followingService = followingService;
        this.readService = readService;
        this.commentService = commentService;
    }

    public List<StoryDTO> getAll() {
        return storyRepository.findAll()
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "storiesCache", key = "{#page, #size, #searchCriteria.hashCode(), #sortBy}")
    public Page<StoryDTO> getPage(int page, int size, SearchCriteria searchCriteria, String sortBy) {
        return searchCriteria.areFieldsEmpty() ?  getPageWithoutCriteria(page, size, sortBy)
            : getPageWithCriteria(page, size, searchCriteria, sortBy);
    }

    private PageImpl<StoryDTO> getPageWithoutCriteria(int page, int size, String sortBy) {
        Sort sort = getSortType(sortBy);
        Pageable pageable = PageRequest.of(page -1, size, sort);
        Page<Story> storiesPage = storyRepository.findAllWithChapters(pageable);
        List<StoryDTO> daos = storiesPage.getContent().stream()
                .map(this::from)
                .collect(Collectors.toList());
        return new PageImpl<>(daos, pageable, storiesPage.getTotalElements());
    }

    private PageImpl<StoryDTO> getPageWithCriteria(int page, int size, SearchCriteria searchCriteria, String sortBy) {
        Sort sort = getSortType(sortBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Specification<Story> spec = buildSpecification(searchCriteria);
        Page<Story> storiesPage = storyRepository.findAll(spec, pageable);
        List<StoryDTO> daos = storiesPage.getContent().stream()
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

    private Specification<Story> buildSpecification(SearchCriteria searchCriteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchCriteria.getTitle() != null && !searchCriteria.getTitle().trim().isEmpty()) {
                predicates.add(builder.like(
                        builder.lower(root.get("title")), "%" + searchCriteria.getTitle().toLowerCase() + "%"
                ));
            }
            if (searchCriteria.getAuthor() != null && !searchCriteria.getAuthor().trim().isEmpty()) {
                predicates.add(builder.like(
                        builder.lower(root.get("author").get("username")), "%" + searchCriteria.getAuthor().toLowerCase() + "%"
                ));
            }
            if (searchCriteria.getDescription() != null && !searchCriteria.getDescription().trim().isEmpty()) {
                predicates.add(builder.like(
                        builder.lower(root.get("description")), "%" + searchCriteria.getDescription().toLowerCase() + "%"
                ));
            }
            if (searchCriteria.getFandoms() != null && !searchCriteria.getFandoms().isEmpty()) {
                for (String fandom : searchCriteria.getFandoms()) {
                    predicates.add(builder.like(
                            builder.lower(root.get("fandoms")), "%" + fandom.toLowerCase() + "%"
                    ));
                }
            }
            if (searchCriteria.getTags() != null && !searchCriteria.getTags().isEmpty()) {
                for (String tag : searchCriteria.getTags()) {
                    predicates.add(builder.like(
                            builder.lower(root.get("tags")), "%" + tag.toLowerCase() + "%"
                    ));
                }
            }
            if (searchCriteria.getRating() != null && !searchCriteria.getRating().isEmpty()) {
                predicates.add(builder.equal(root.get("rating"), searchCriteria.getRating()));
            }
            predicates.add(builder.isNotEmpty(root.get("chapters")));
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }


        @Cacheable(value = "userStoriesCache", key = "{#id, #page, #size}")
        public Page<StoryDTO> getMyStories(Long id, int page, int size) {
            Sort sort = Sort.by(Sort.Order.desc("updateDt"));
            Pageable pageable = PageRequest.of(page -1, size, sort);
            Page<Story> stories = storyRepository.findAllByAuthorId(id, pageable);
            List<StoryDTO> daos = new ArrayList<>();
            stories.forEach(story -> daos.add(from(story)));
            return  new PageImpl<>(daos, pageable, stories.getTotalElements());
        }


    public StoryDTO getStory(Long id) {
        return storyRepository.findById(id)
                .map(this::from)
                .orElseThrow(() -> new StoryException(StoryError.STORY_NOT_FOUND));
    }

    @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true)
    public void saveStory(StoryDTO story) {
        storyRepository.save(from(story));
    }

    public StoryDTO from(Story story) {
        StoryDTO s = new StoryDTO();
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
        s.setComment(story.isAllowComments());
        s.setGuestComment(story.isAllowGuestComments());
        s.setChaptersNumber(chapterService.getChapterNumber(story.getId()));
        s.setStoryRating(story.getRating());

        if (story.getAuthor() != null) {
            AuthorDTO authorDTO = new AuthorDTO();
            authorDTO.setUsername(story.getAuthor().getUsername());
            authorDTO.setId(story.getAuthor().getId());
        }

        return s;
    }

    public Story from(StoryDTO storyDTO) {
        Story story = new Story();
        story.setDescription(storyDTO.getDescription());
        story.setTitle(storyDTO.getTitle());
        updateStoryTagsAndFandoms(story, storyDTO);
        story.setFinished(storyDTO.isFinished());
        story.setPublishDt(storyDTO.getPublishDt());
        story.setUpdateDt(storyDTO.getUpdateDt());
        story.setRating(storyDTO.getStoryRating());
        story.setAllowComments(storyDTO.isComment());
        story.setAllowGuestComments(storyDTO.isGuestComment());

        if (storyDTO.getAuthorId() > 0) {
            Optional<Author> author = authorService.find(storyDTO.getAuthorId());
            author.ifPresent(story::setAuthor);
        }

        return story;
    }

    public void updateStoryTagsAndFandoms(Story story, StoryDTO storyDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (Objects.nonNull(storyDTO.getTags())) {
                String tagsJson = objectMapper.writeValueAsString(storyDTO.getTags());
                story.setTags(tagsJson);
            } else story.setTags("[\"\"]");
            if (Objects.nonNull(storyDTO.getFandoms())) {
                String fandomsJson = objectMapper.writeValueAsString(storyDTO.getFandoms());
                story.setFandoms(fandomsJson);
            } else story.setFandoms("[\"\"]");
        } catch (Exception e) {
            log.warn(e.getMessage());
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
    @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true)
    public void deleteStory(Long id) {
        commentService.deleteByStoryId(id);
        chapterService.deleteChaptersForStory(id);
        favoriteService.delete(id);
        followingService.delete(id);
        readService.delete(id);
        storyRepository.deleteStoryById(id);
    }

    @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true)
    public void editStory(StoryDTO storyDTO) {
        Optional<Story> story = storyRepository.findById(storyDTO.getId());
        if (story.isPresent()) {
            Story s = story.get();
            s.setTitle(storyDTO.getTitle());
            s.setDescription(storyDTO.getDescription());
            s.setFandoms(joinAndAddQuotes(storyDTO.getFandoms()));
            s.setFinished(storyDTO.isFinished());
            s.setTags(joinAndAddQuotes(storyDTO.getTags()));
            s.setRating(storyDTO.getStoryRating());
            s.setAllowGuestComments(storyDTO.isGuestComment());
            s.setAllowComments(storyDTO.isComment());
             storyRepository.save(s);
        }
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
