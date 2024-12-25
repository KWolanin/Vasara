import { api } from "../boot/axios";
import { Author } from "../types/Author";

export const fetchAuthors = async (): Promise<Author[]> => {
  try {
    const response = await api.get("/authors/all");
    return response.data;
  } catch (error) {
    console.error("Error fetching authors:", error);
    throw error;
  }
};

export const getAuthorNameById = async (id: number): Promise<String> => {
  try {
    const response = await api.get(`/authors/${id}/name`);
    return response.data;
  } catch (error) {
    console.error("Error fetching author's name:", error);
    throw error;
  }
};
