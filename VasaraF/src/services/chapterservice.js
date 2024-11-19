import { api } from "../boot/axios";

export const createChapter = async (chapterData) => {
  try {
    console.log(chapterData);
    const response = await api.post("/chapters/add", chapterData);
    return response.data;
  } catch (error) {
    console.error("Error creating chapter:", error);
    throw error;
  }
};

export const fetchChapter = async (storyId, chapterNo) => {
  try {
    const response = await api.get(`/chapters/read/${storyId}/${chapterNo}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};
