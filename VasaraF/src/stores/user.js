import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const id = ref(null);
  const username = ref(null);
  const login = ref(null);
  const email = ref(null);

  const saveUser = (inputId, inputUsername, inputLogin, inputEmail) => {
    id.value = inputId;
    username.value = inputUsername;
    login.value = inputLogin;
    email.value = inputEmail;
    localStorage.setItem(
      "user",
      JSON.stringify({
        id: inputId,
        username: inputUsername,
        login: inputLogin,
        email: inputEmail,
      })
    );
  };

  const loadUser = () => {
    const user = JSON.parse(localStorage.getItem("user"));
    if (user) {
      id.value = user.id;
      username.value = user.username;
      login.value = user.login;
      email.value = user.email;
    }
  };

  const logout = () => {
    id.value = null;
    username.value = null;
    login.value = null;
    email.value = null;
    localStorage.removeItem("user");
    localStorage.removeItem("token");
  };

  const updateEmail = (emailInput) => {
    email.value = emailInput;
    localStorage.setItem(
      "user",
      JSON.stringify({
        id: id.value,
        username: username.value,
        login: login.value,
        email: email.value,
      })
    );
  };

  const updateUsername = (usernameInput) => {
    username.value = usernameInput;
    localStorage.setItem(
      "user",
      JSON.stringify({
        id: id.value,
        username: username.value,
        login: login.value,
        email: email.value,
      })
    );
  };

  return {
    id,
    username,
    login,
    email,
    saveUser,
    loadUser,
    logout,
    updateEmail,
    updateUsername,
  };
});
