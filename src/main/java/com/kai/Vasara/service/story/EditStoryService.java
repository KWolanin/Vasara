package com.kai.Vasara.service.story;

import com.kai.Vasara.entity.story.Story;
import com.kai.Vasara.mapper.Mapper;
import com.kai.Vasara.model.story.StoryDTO;
import com.kai.Vasara.repository.story.StoryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EditStoryService {

    private final StoryRepository storyRepository;
    private final Mapper mapper;


    public EditStoryService(StoryRepository storyRepository, Mapper mapper) {
        this.storyRepository = storyRepository;
        this.mapper = mapper;
    }

    @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true)
    public void saveStory(StoryDTO story) {
        storyRepository.save(mapper.storyDTOToStory(story));
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
            s.setRating(storyDTO.getRating());
            s.setAllowGuestComments(storyDTO.isGuestComment());
            s.setAllowComments(storyDTO.isComment());
            storyRepository.save(s);
        }
    }

    public String joinAndAddQuotes(List<String> inputList) {
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
}
