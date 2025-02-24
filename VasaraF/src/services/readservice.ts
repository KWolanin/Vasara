import { StoryPage } from "src/types/StoryPage";
import { api } from "../boot/axios";
import { useUserStore } from "../stores/user";



export const addToReads = async (user: number, story: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/reads/addRead`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error during adding/removing to read later list:", error);
    throw error;
  }
}


export const isReads = async (user: number, story: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/reads/isRead`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error checking following stories:", error);
    throw error;
  }
}


export const findMyReads = async (page: number, size: number, id: number): Promise<StoryPage> => {
  try {
    const response = await api.get(`/reads/my/${id}?page=${page}&size=${size}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};

export const countReads = async () : Promise<number> => {
  try {
    const userStore = useUserStore();
    const id = userStore.id;
    const response = await api.get(`/reads/count/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error counting my follows stories:", error);
    throw error;
  }
}
