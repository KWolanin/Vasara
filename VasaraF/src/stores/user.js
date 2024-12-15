import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => ({
    id: null,
    username: null,
    login: null,
  }),
  actions: {
    saveUser(id, username, login) {
      this.id = id;
      this.username = username;
      this.login = login;
      const user = { id, username, login };
      localStorage.setItem("user", JSON.stringify(user));
    },
    loadUser() {
      const user = JSON.parse(localStorage.getItem("user"));
      if (user) {
        this.id = user.id;
        this.username = user.username;
        this.login = user.login;
      }
    },
    logout() {
      this.id = null;
      this.username = null;
      this.login = null;
      localStorage.removeItem("user");
    },
  },
});
