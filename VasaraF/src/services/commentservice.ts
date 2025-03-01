import { api } from "../boot/axios";
import { ChapterComment, ChapterPermission } from "../types/ChapterComment";

export const createComment = async (comment: Omit<ChapterComment, 'id'>): Promise<void> => {
  try {
    const response = await api.post("/comments/add", comment);
    return response.data;
  } catch (error) {
    console.error("Error creating comment:", error);
    throw error;
  }
};

export const getComments = async (chapterId: number): Promise<ChapterComment[]> => {
  try {
    const response = await api.get(`/comments/${chapterId}`);
    return response.data;
  } catch (error) {
    console.error("Error getting comment:", error);
    throw error;
  }
};

export const checkPermissions = async (chapterId: number): Promise<ChapterPermission> => {
  try {
    const response = await api.get(`/comments/perms/${chapterId}`);
    return response.data;
  } catch (error) {
    console.error("Error getting comment:", error);
    throw error;
  }
}

export const deleteCommentFromDb = async (chapterId: number): Promise<void> => {
  try {
    const response = await api.delete(`/comments/${chapterId}`);
    return response.data;
  } catch (error) {
    console.error("Error deletting comment:", error);
    throw error;
  }
}
