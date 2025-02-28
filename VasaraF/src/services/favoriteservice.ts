import { StoryPage } from "src/types/StoryPage";
import { api } from "../boot/axios";
import { useUserStore } from "../stores/user";



export const addToFavorite = async (user: number, story: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/favorites/add`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error during adding/removing to favorites:", error);
    throw error;
  }
}


export const isFav = async (user: number, story: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/favorites/is`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error checking favorites:", error);
    throw error;
  }
}


export const findMyFavs = async (page: number, size: number, id: number): Promise<StoryPage> => {
  try {
    const response = await api.get(`/favorites/my/${id}?page=${page}&size=${size}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};

export const countFavs = async () : Promise<number> => {
  try {
    const userStore = useUserStore();
    const id = userStore.id;
    const response = await api.get(`/favorites/count/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error counting my favs stories:", error);
    throw error;
  }
}
