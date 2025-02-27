import { AuthorPage } from "src/types/AuthorPage";
import { api } from "../boot/axios";
import { useUserStore } from "../stores/user";



export const addToFollows = async (following: number, followed: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/authorsfollows/add`,
      { following: following, followed: followed });
    return response.data;
  } catch (error) {
    console.error("Error during adding/removing author to following:", error);
    throw error;
  }
}

export const isFollow = async (following: number, followed: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/authorsfollows/is`, { following: following, followed: followed });
    return response.data;
  } catch (error) {
    console.error("Error checking following authors:", error);
    throw error;
  }
}


export const findMyFollowedAuthors = async (page: number, size: number, id: number): Promise<AuthorPage> => {
  try {
    const response = await api.get(`/authorsfollows/my/${id}?page=${page}&size=${size}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching stories:", error);
    throw error;
  }
};

export const countFollowedAuthors = async () : Promise<number> => {
  try {
    const userStore = useUserStore();
    const id = userStore.id;
    const response = await api.get(`/authorsfollows/count/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error counting my favs stories:", error);
    throw error;
  }
}
