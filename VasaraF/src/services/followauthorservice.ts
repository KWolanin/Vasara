import { api } from "../boot/axios";



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
