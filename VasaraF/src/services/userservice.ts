import { useUserStore } from "../stores/user";
import { api } from "../boot/axios";
import { Author } from "../types/Author";
import { RegisterRequest } from "../types/RegisterRequest";
import { UpdateAuthorRequest } from "src/types/UpdateAuthorRequest";

export const login = async (loginData: Author): Promise<void> => {
  try {
    const response = await api.post("/authors/login", loginData);
    const userStore = useUserStore();
    const { id, login, username, email } = response.data;
    const token = response.data.token;
    userStore.saveUser(id, username, login, email);
    localStorage.setItem("token", token);
  } catch (error) {
    console.error("Error logging in:", error);
    throw error;
  }
};

export const register = async (
  registerData: RegisterRequest
): Promise<void> => {
  try {
    await api.post("/authors/register", registerData);
  } catch (error) {
    console.error("Error registering:", error);
    throw error;
  }
};

export const change = async (updateAuthorRequest : UpdateAuthorRequest) : Promise<void> => {
  try {
    await api.patch(`/authors/update`, updateAuthorRequest);
  } catch (error) {
    console.error("Error changing user details:", error);
    throw error;
  }
}
