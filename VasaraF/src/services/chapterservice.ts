import { api } from "../boot/axios";
import { Chapter } from "../types/Chapter";

export const createChapter = async (chapterData: Chapter | Omit<Chapter, 'id'>): Promise<void> => {
  try {
    await api.post("/chapters/add", chapterData);
  } catch (error) {
    console.error("Error creating chapter:", error);
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

export const fetchChapterWithParagraphs = async (storyId, chapterNo, offset, limit) => {
  const response = await api.get(`/chapters/read/paragraphs/${storyId}/${chapterNo}`, {
    params: { offset, limit }
  });
  //console.log(response.data)
  return response.data;
};

export const fetchChaptersForStory = async (
  storyId: number): Promise<Chapter[]> => {
  try {
    const response = await api.get(`/chapters/all/${storyId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching chapters data:", error);
    throw error;
  }
};

export const isNextOrPrevious = async (
  storyId: number,
  chapterNo: number
): Promise<boolean> => {
  try {
    const response = await api.get(
      `/chapters/navigable/${storyId}/${chapterNo}`
    );
    return response.data;
  } catch (error) {
    console.error(
      "Error checking is next or previous chapter available:",
      error
    );
    throw error;
  }
};

export const updateChaptersOrder = async (chapters : Chapter[]) => {
  try {
    const response = await api.patch('/chapters/order', chapters)
    return response.data;
  } catch (error) {
    console.error("Error updating chapters order:", error);
    throw error;
  }
}

export const deleteChapterFromDb = async (id: number): Promise<void> => {
  try {
    await api.delete(`/chapters/delete/${id}`);
  } catch (error) {
    console.error("Error deleting chapter:", error);
    throw error;
  }
}
