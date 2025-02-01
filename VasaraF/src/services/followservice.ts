import { StoryPage } from "src/types/StoryPage";
import { api } from "../boot/axios";


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
    console.log(response)
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};
