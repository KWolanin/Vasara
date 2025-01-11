import { api } from "../boot/axios";
import { useUserStore } from "src/stores/user";
import { Story } from "../types/Story";
import { StoryPage } from "../types/StoryPage";


export const fetchStories = async (page: number, size: number): Promise<StoryPage> => {
  try {
    const response = await api.get(`/stories/all?page=${page}&size=${size}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};

export const fetchMyStories = async (page: number, size: number): Promise<StoryPage> => {
  try {
    const userStore = useUserStore();
    const id = userStore.id;
    const response = await api.get(`/stories/my/${id}?page=${page}&size=${size}`);
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

export const count = async () : Promise<number> => {
  try {
    const response = await api.get("/stories/count");
    return response.data;
  } catch (error) {
    console.error("Error counting stories:", error);
    throw error;
  }
}

export const countMines = async () : Promise<number> => {
  try {
    const userStore = useUserStore();
    const id = userStore.id;
    const response = await api.get(`/stories/mines/count/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error counting my stories:", error);
    throw error;
  }
}
