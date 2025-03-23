package com.kai.Vasara.service.chapter;

import com.kai.Vasara.entity.chapter.Chapter;
import com.kai.Vasara.model.chapter.ChapterWithParagraphsDTO;
import com.kai.Vasara.model.chapter.ParagraphDTO;
import com.kai.Vasara.model.story.StoryDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetSplitChapterService {

    private final CacheService service;

    public GetSplitChapterService(CacheService service) {
        this.service = service;
    }

    @Transactional
    public ChapterWithParagraphsDTO getSplitChapter(Long storyId, Long chapterNo, int offset, int limit) {
       return toChapterWithParagraphsDTO(service.getSplitChapter(storyId, chapterNo), offset, limit);
    }

    // todo: implement mapstruck to reduce boiler code
    private ChapterWithParagraphsDTO toChapterWithParagraphsDTO(Chapter chapter, int offset, int limit) {
        ChapterWithParagraphsDTO dto = new ChapterWithParagraphsDTO();
        dto.setId(chapter.getId());
        dto.setChapterNo(chapter.getChapterNo());
        dto.setChapterTitle(chapter.getChapterTitle());
        dto.setStoryId(chapter.getStory().getId());
        dto.setPublished(chapter.getPublished());
        dto.setUpdated(chapter.getUpdated());
        setSplitParagraphs(chapter, offset, limit, dto);
        if (chapter.getStory() != null) {
            setStoryDTO(chapter, dto);
        }
        dto.setNext(chapter.getStory().getChapters().size() > chapter.getChapterNo());
        dto.setPrevious(chapter.getChapterNo() > 1);
        return dto;
    }

    private void setSplitParagraphs(Chapter chapter, int offset, int limit, ChapterWithParagraphsDTO dto) {
        List<ParagraphDTO> allParagraphs = splitIntoParagraphs(chapter.getContent());
        List<ParagraphDTO> paginatedParagraphs = allParagraphs.stream()
                .skip(offset)
                .limit(limit)
                .toList();
        dto.setParagraphs(paginatedParagraphs);
    }

    private static void setStoryDTO(Chapter chapter, ChapterWithParagraphsDTO dto) {
        StoryDTO storyDTO = new StoryDTO();
        storyDTO.setId(chapter.getStory().getId());
        storyDTO.setTitle(chapter.getStory().getTitle());
        storyDTO.setAuthorName(chapter.getStory().getAuthor().getLogin());
        storyDTO.setAuthorId(chapter.getStory().getAuthor().getId());
        dto.setStoryDTO(storyDTO);
        dto.setStoryId(storyDTO.getId());
    }

    private List<ParagraphDTO> splitIntoParagraphs(String htmlContent) {
        List<ParagraphDTO> paragraphs = new ArrayList<>();
        Document doc = Jsoup.parse(htmlContent);
        Elements elements = doc.body().children();
        int paragraphId = 1;
        for (Element element : elements) {
            if (!element.text().trim().isEmpty() || "<hr>".equals(element.outerHtml())) {
                paragraphs.add(new ParagraphDTO(paragraphId++, element.outerHtml()));
            }
        }
        return paragraphs;
    }
}
