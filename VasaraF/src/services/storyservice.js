import { api } from "../boot/axios";

export const fetchStories = async () => {
  try {
    const response = await api.get("/stories/all");
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};

export const fetchMyStories = async () => {
  try {
    const id = 1;
    const response = await api.get(`/stories/my/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};

export const createStory = async (storyData) => {
  try {
    const response = await api.post("/stories/add", storyData);
    return response.data;
  } catch (error) {
    console.error("Error creating story:", error);
    throw error;
  }
};

export const updateStory = async (storyData) => {
  try {
    const response = await api.patch("/stories/edit", storyData);
    return response.data;
  } catch (error) {
    console.error("Error editing story:", error);
    throw error;
  }
};

export const deleteStory = async (id) => {
  try {
    const response = await api.delete(`/stories/delete/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error deleting story:", error);
    throw error;
  }
};
