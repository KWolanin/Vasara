import { api } from "../boot/axios";

export const fetchAuthors = async () => {
  try {
    const response = await api.get("/authors/all");
    return response.data;
  } catch (error) {
    console.error("Error fetching authors:", error);
    throw error;
  }
};

export const getAuthorNameById = async (id) => {
  try {
    const response = await api.get(`/authors/${id}/name`);
    return response.data;
  } catch (error) {
    console.error("Error fetching authors:", error);
    throw error;
  }
};
