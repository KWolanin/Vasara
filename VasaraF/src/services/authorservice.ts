import { api } from "../boot/axios";
import { Author } from "../types/Author";

export const getAuthor = async (id: number): Promise<Author> => {
  try {
    const response = await api.get(`/authors/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching author:", error);
    throw error;
  }
};

export const getAuthorDescriptionById = async (id: number): Promise<string> => {
  try {
    const response = await api.get(`/authors/${id}/desc`);
    return response.data;
  } catch (error) {
    console.error("Error fetching author's description:", error);
    throw error;
  }
};
