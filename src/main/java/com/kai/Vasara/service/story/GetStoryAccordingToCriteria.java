package com.kai.Vasara.service.story;

import com.kai.Vasara.model.SearchCriteria;
import com.kai.Vasara.model.story.StoryInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class GetStoryAccordingToCriteria {

    private final GetStoryService getStoryService;

    public GetStoryAccordingToCriteria(GetStoryService getStoryService) {
        this.getStoryService = getStoryService;
    }

    @Cacheable(value = "storiesCache", key = "{#page, #size, #searchCriteria.hashCode(), #sortBy}")
    public Page<StoryInfo> getPage(int page, int size, SearchCriteria searchCriteria, String sortBy) {
        return searchCriteria.areFieldsEmpty() ? getStoryService.getPageWithoutCriteria(page, size, sortBy)
                : getStoryService.getPageWithCriteria(page, size, searchCriteria, sortBy);
    }
}
