import { useUserStore } from "../stores/user";
import { api } from "../boot/axios";
import { Author } from "../types/Author";
import { RegisterRequest } from "../types/RegisterRequest";

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


export const changeEmail = async (email: string, id: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/authors/changeEmail`, { email: email, id: id });
    return response.data;
  } catch (error) {
    console.error("Error changing email:", error);
    return error;
  }
}

export const changeUsername = async (username: string, id: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/authors/changeUsername`, { username: username, id: id });
    return response.data;
  } catch (error) {
    console.error("Error changing username:", error);
    return error;
  }
}

export const changePassword = async (password: string, id: number) : Promise<Boolean> => {
  try {
    const response = await api.post(`/authors/changePassword`, { password: password, id: id });
    return response.data;
  } catch (error) {
    console.error("Error changing password:", error);
    return error;
  }
}

export const changeDesc = async (id: number, description: string) : Promise<Boolean> => {
  try {
    const response = await api.post(`/authors/changeDescription`, { description: description, id: id });
    return response.data;
  } catch (error) {
    console.error("Error changing description:", error);
    return error;
  }
}
