import { useUserStore } from "../stores/user";
import { api } from "../boot/axios";

export const login = async (loginData) => {
  try {
    const response = await api.post("/authors/login", loginData);
    console.log(response);
    const userStore = useUserStore();
    const { id, username, login } = response.data;
    userStore.saveUser(id, username, login);
    return response.data;
  } catch (error) {
    console.error("Error logging in:", error);
    return error;
  }
};