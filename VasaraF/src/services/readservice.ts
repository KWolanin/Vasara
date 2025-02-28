import { StoryPage } from "src/types/StoryPage";
import { api } from "../boot/axios";
import { useUserStore } from "../stores/user";



export const addToReads = async (user: number, story: number) : Promise<boolean> => {
  try {
    const response = await api.post(`/reads/add`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error during adding/removing to read later list:", error);
    throw error;
  }
}


export const isReads = async (user: number, story: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/reads/is`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error checking read later stories:", error);
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
    console.error("Error counting my read later stories:", error);
    throw error;
  }
}

export const getRandom = async () : Promise<Map<number, string>> => {
  try {
    const userStore = useUserStore();
    const id = userStore.id;
    const response = await api.get(`/reads/random/${id}`);
    return response.data;
  } catch (error) {
    // when user has no read later stories, error is excepted
    throw error;
  }
}
