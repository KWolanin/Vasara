import { api } from "../boot/axios";
import { Chapter } from "../types/Chapter";

export const createChapter = async (chapterData: Chapter): Promise<boolean> => {
  try {
    const response = await api.post("/chapters/add", chapterData);
    return response.data;
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

export const isNextOrPrevious = async (
  storyId: number,
  chapterNo: number
): Promise<boolean> => {
  try {
    const response = await api.get(
      `/chapters/isNextOrPrevious/${storyId}/${chapterNo}`
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
