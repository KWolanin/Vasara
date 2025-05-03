import { boot } from "quasar/wrappers";
import axios from "axios";
import { useUserStore } from "src/stores/user";
import { showNotification } from "src/utilsTS/notify";

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 403) {
      const userStore = useUserStore();
      userStore.logout();
      localStorage.removeItem("token");
      showNotification("Session expired, please login again", "negative")
      window.location.href = "/#/login";
    }
    return Promise.reject(error);
  }
);


export default boot(({ app }) => {
  app.config.globalProperties.$axios = axios;
  app.config.globalProperties.$api = api;
});

export { api };
