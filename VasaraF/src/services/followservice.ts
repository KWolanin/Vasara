import { StoryPage } from "src/types/StoryPage";
import { api } from "../boot/axios";
import { useUserStore } from "../stores/user";



export const addToFollows = async (user: number, story: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/follows/addFollow`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error during adding/removing to favorites:", error);
    return error;
  }
}


export const isFollow = async (user: number, story: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/follows/isFollowed`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error checking following stories:", error);
    return error;
  }
}


export const findMyFollows = async (page: number, size: number, id: number): Promise<StoryPage> => {
  try {
    const response = await api.get(`/follows/my/${id}?page=${page}&size=${size}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};


export const countFollows = async () : Promise<number> => {
  try {
    const userStore = useUserStore();
    const id = userStore.id;
    const response = await api.get(`/follows/count/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error counting my follows stories:", error);
    throw error;
  }
}
