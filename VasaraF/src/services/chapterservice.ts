import { ChapterInfo } from "src/types/ChapterInfo";
import { api } from "../boot/axios";
import { Chapter } from "../types/Chapter";

export const createChapter = async (
  chapterData: Chapter | Omit<Chapter, "id">
): Promise<void> => {
  try {
    await api.post("/chapters/add", chapterData);
  } catch (error) {
    console.error("Error creating chapter:", error);
    throw error;
  }
};

export const editChapter = async (
  chapterData: Chapter | Omit<Chapter, "id">
): Promise<void> => {
  try {
    await api.patch("/chapters/edit", chapterData);
  } catch (error) {
    console.error("Error editing chapter:", error);
    throw error;
  }
};

export const fetchChapter = async (
  storyId: number,
  chapterNo: number
): Promise<Chapter> => {
  try {
    const response = await api.get(`/chapters/read/${storyId}/${chapterNo}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching chapter:", error);
    throw error;
  }
};

export const fetchChapterWithParagraphs = async (
  storyId: number,
  chapterNo: number,
  offset: number,
  limit: number
): Promise<Chapter> => {
  try {
    const response = await api.get(
      `/chapters/read/paragraphs/${storyId}/${chapterNo}`,
      {
        params: { offset, limit },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching chapter with paragraphs:", error);
    throw error;
  }
};

export const fetchChaptersForStory = async (
  storyId: number
): Promise<ChapterInfo[]> => {
  try {
    const response = await api.get(`/chapters/all/${storyId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching chapters data:", error);
    throw error;
  }
};

export const updateChaptersOrder = async (chapters: ChapterInfo[]) : Promise<void> => {
  try {
    await api.patch("/chapters/order", chapters);
  } catch (error) {
    console.error("Error updating chapters order:", error);
    throw error;
  }
};

export const deleteChapterFromDb = async (id: number): Promise<void> => {
  try {
    await api.delete(`/chapters/delete/${id}`);
  } catch (error) {
    console.error("Error deleting chapter:", error);
    throw error;
  }
};
