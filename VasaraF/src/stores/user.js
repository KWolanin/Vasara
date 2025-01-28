import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => ({
    id: null,
    username: null,
    login: null,
    email: null
  }),
  actions: {
    saveUser(id, username, login, email) {
      this.id = id;
      this.username = username;
      this.login = login;
      this.email = email;
      const user = { id, username, login, email };
      localStorage.setItem("user", JSON.stringify(user));
    },
    loadUser() {
      const user = JSON.parse(localStorage.getItem("user"));
      if (user) {
        this.id = user.id;
        this.username = user.username;
        this.login = user.login;
        this.email = user.email;
      }
    },
    logout() {
      this.id = null;
      this.username = null;
      this.login = null;
      this.email = null;
      localStorage.removeItem("user");
    },
    updateEmail(email) {
      this.email = email;
      localStorage.setItem("user", JSON.stringify(this.$state)); // Używamy this.$state, by zapisać całą strukturę stanu
    },
    updateUsername(username) {
      this.username = username;
      localStorage.setItem("user", JSON.stringify(this.$state)); // Używamy this.$state, by zapisać całą strukturę stanu
    }
  },
});
