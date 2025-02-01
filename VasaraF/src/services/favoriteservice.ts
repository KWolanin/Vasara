import { StoryPage } from "src/types/StoryPage";
import { api } from "../boot/axios";


export const addToFavorite = async (user: number, story: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/favorites/addfavorite`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error during adding/removing to favorites:", error);
    return error;
  }
}


export const isFav = async (user: number, story: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/favorites/isFav`, { user: user, story: story });
    return response.data;
  } catch (error) {
    console.error("Error checking favorites:", error);
    return error;
  }
}


export const findMyFavs = async (page: number, size: number, id: number): Promise<StoryPage> => {
  try {
    const response = await api.get(`/favorites/my/${id}?page=${page}&size=${size}`);
    console.log(response)
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};
