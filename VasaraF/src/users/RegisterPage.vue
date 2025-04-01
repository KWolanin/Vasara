<template>
  <div class="center q-ma-md">
    <div class="text-center">
      <h3 class="title q-mb-lg q-pb-lg">Vasara</h3>
    </div>
    <q-card class="q-pa-md card content" >
      <q-form @submit="registerUser" class="q-gutter-md" autofocus>
        <div v-if="msg" class="msg">{{ msg }}</div>
        <q-input filled v-model="login" label="Login" color="burgund" />
        <q-input filled v-model="username" label="Username" color="burgund" />
        <q-input
          filled
          v-model="email"
          label="Email"
          type="email"
          color="burgund"
        />
        <q-input
          filled
          v-model="password"
          label="Password"
          type="password"
          color="burgund"
        />
        <div>
          <q-btn
            label="Register"
            type="submit"
            color="primary"
            class="btn bg-yellow-4"
            flat
          />
        </div>
      </q-form>
      <div>
        <router-link to="login">
          <p class="guest-account q-mt-sm">Have an account? Login here</p>
        </router-link>
      </div>
    </q-card>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { register } from "src/services/userservice";
import { useRouter } from "vue-router";
import { showNotification } from "src/utilsTS/notify";

const router = useRouter();

const login = ref("");
const password = ref("");
const username = ref("");
const email = ref("");

const msg = ref("");

const registerUser = () => {
  if (!username.value || !password.value || !login.value || !email.value) {
    msg.value = "All fields are required";
    return;
  }
  register({
    username: username.value,
    login: login.value,
    password: password.value,
    email: email.value,
  })
    .then(() => {
      msg.value = "";
      showNotification("Account created successfully", "positive");
      router.push("/login");
    })
    .catch((error) => {
      msg.value =
        `Error during registration: ${error.response.data.message}`.toUpperCase();
    });
};
</script>

<style scoped>
.center {
  justify-items: center;
}

.content {
  width: 40%;
}

.btn {
  font-family: "Farro", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: #333 !important;
}

a:visited {
  color: #333;
}


.title {
  font-family: "WindSong", cursive;
  font-size: 7rem;
  text-align: center;
  color: #333;
}

.guest-account p {
  font-size: xx-small;
  margin-bottom: 0 !important;
  margin-top: 0 !important;
}

a,
a:visited {
  color: #333;
}

.msg {
  color: red;
  margin-top: 10px;
  text-align: center;
  font-size: small;
  font-weight: 700;
}
</style>
