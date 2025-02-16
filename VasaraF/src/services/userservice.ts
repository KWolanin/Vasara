import { useUserStore } from "../stores/user";
import { api } from "../boot/axios";
import { Author } from "../types/Author";
import { RegisterRequest } from "../types/RegisterRequest";
import { UpdateAuthorRequest } from "src/types/UpdateAuthorRequest";

export const login = async (loginData: Author): Promise<boolean> => {
  try {
    const response = await api.post("/authors/login", loginData);
    const userStore = useUserStore();
    const { id, username, login, email } = response.data;
    userStore.saveUser(id, username, login, email);
    return response.data;
  } catch (error) {
    console.error("Error logging in:", error);
    return error;
  }
};

export const register = async (
  registerData: RegisterRequest
): Promise<Boolean> => {
  try {
    const response = await api.post("/authors/register", registerData);
    return response.data;
  } catch (error) {
    console.error("Error registering:", error);
    return error;
  }
};

export const change = async (updateAuthorRequest : UpdateAuthorRequest) : Promise<Boolean> => {
  try {
    const response = await api.patch(`/authors`, updateAuthorRequest);
    return response.data;
  } catch (error) {
    console.error("Error changing description:", error);
    return error;
  }
}
