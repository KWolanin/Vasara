import { StoryPage } from "src/types/StoryPage";
import { api } from "../boot/axios";


export const addToReads = async (user: number, story: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/reads/addRead`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error during adding/removing to read later list:", error);
    return error;
  }
}


export const isReads = async (user: number, story: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/reads/isRead`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error checking following stories:", error);
    return error;
  }
}


export const findMyReads = async (page: number, size: number, id: number): Promise<StoryPage> => {
  try {
    const response = await api.get(`/reads/my/${id}?page=${page}&size=${size}`);
    console.log(response)
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};
