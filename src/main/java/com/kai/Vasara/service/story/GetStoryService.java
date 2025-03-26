package com.kai.Vasara.service.story;

import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.exception.story.StoryError;
import com.kai.Vasara.exception.story.StoryException;
import com.kai.Vasara.model.SearchCriteria;
import com.kai.Vasara.model.story.StoryDTO;
import com.kai.Vasara.model.story.StoryInfo;
import com.kai.Vasara.repository.story.StoryRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Component
@Slf4j
public class GetStoryService {

    private final StoryRepository storyRepository;
    private final StoryMapper mapper;

    @Autowired
    public GetStoryService(StoryRepository storyRepository,
                           StoryMapper mapper) {
        this.storyRepository = storyRepository;
        this.mapper = mapper;
    }

    public List<StoryDTO> getAll() {
        return storyRepository.findAll()
                .stream()
                .map(mapper::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PageImpl<StoryInfo> getPageWithoutCriteria(int page, int size, String sortBy) {
        Sort sort = getSortType(sortBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Story> storiesPage = storyRepository.findAllWithChapters(pageable);
        List<StoryInfo> daos = new ArrayList<>(storiesPage.stream().map(StoryInfo::from).toList());
        return new PageImpl<>(daos, pageable, storiesPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PageImpl<StoryInfo> getPageWithCriteria(int page, int size, SearchCriteria searchCriteria, String sortBy) {
        Sort sort = getSortType(sortBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Specification<Story> spec = buildSpecification(searchCriteria);
        Page<Story> storiesPage = storyRepository.findAll(spec, pageable);
        List<StoryInfo> daos = new ArrayList<>(storiesPage.stream().map(StoryInfo::from).toList());
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
    @Transactional(readOnly = true)
    public Page<StoryInfo> getMyStories(Long id, int page, int size) {
        Sort sort = Sort.by(Sort.Order.desc("updateDt"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Story> stories = storyRepository.findAllByAuthorId(id, pageable);
        List<StoryInfo> daos = new ArrayList<>(stories.stream().map(StoryInfo::from).toList());
        return new PageImpl<>(daos, pageable, stories.getTotalElements());
    }

    public StoryDTO getStory(Long id) {
        return storyRepository.findById(id)
                .map(mapper::from)
                .orElseThrow(() -> new StoryException(StoryError.STORY_NOT_FOUND));
    }

    public long count() {
        return storyRepository.count();
    }

    public long countMine(Long id) {
        return storyRepository.countMine(id);
    }
}
