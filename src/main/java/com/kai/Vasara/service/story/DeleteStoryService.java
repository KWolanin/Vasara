package com.kai.Vasara.service.story;

import com.kai.Vasara.repository.story.StoryRepository;
import com.kai.Vasara.service.chapter.EditChapterService;
import com.kai.Vasara.service.comment.CommentService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteStoryService {

    private final StoryRepository storyRepository;
    private final CommentService commentService;
    private final FavoriteServiceStory favoriteServiceStory;
    private final EditChapterService editChapterService;
    private final FollowServiceStory followServiceStory;
    private final ReadLaterService readLaterService;

    public DeleteStoryService(StoryRepository storyRepository, CommentService commentService, FavoriteServiceStory favoriteServiceStory, EditChapterService editChapterService, FollowServiceStory followServiceStory, ReadLaterService readLaterService) {
        this.storyRepository = storyRepository;
        this.commentService = commentService;
        this.favoriteServiceStory = favoriteServiceStory;
        this.editChapterService = editChapterService;
        this.followServiceStory = followServiceStory;
        this.readLaterService = readLaterService;
    }

    @Transactional
    @CacheEvict(value = { "userStoriesCache", "storiesCache" }, allEntries = true)
    public void deleteStory(Long id) {
        commentService.deleteByStoryId(id);
        editChapterService.deleteChaptersForStory(id);
        favoriteServiceStory.delete(id);
        followServiceStory.delete(id);
        readLaterService.delete(id);
        storyRepository.deleteStoryById(id);
    }
}
