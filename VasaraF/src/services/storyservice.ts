import { api } from "../boot/axios";
import { useUserStore } from "src/stores/user";
import { Story } from "../types/Story";

export const fetchStories = async (): Promise<Story[]> => {
  try {
    const response = await api.get("/stories/all");
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};

export const fetchMyStories = async (): Promise<Story[]> => {
  try {
    const userStore = useUserStore();
    const id = userStore.id;
    const response = await api.get(`/stories/my/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};

export const createStory = async (storyData: Story | Omit<Story, "id">): Promise<boolean> => {
  try {
    const response = await api.post("/stories/add", storyData);
    return response.data;
  } catch (error) {
    console.error("Error creating story:", error);
    throw error;
  }
};

export const updateStory = async (storyData: Story | Omit<Story, "id">): Promise<boolean> => {
  try {
    const response = await api.patch("/stories/edit", storyData);
    return response.data;
  } catch (error) {
    console.error("Error editing story:", error);
    throw error;
  }
};

export const deleteStory = async (id: number): Promise<boolean> => {
  try {
    const response = await api.delete(`/stories/delete/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error deleting story:", error);
    throw error;
  }
};
