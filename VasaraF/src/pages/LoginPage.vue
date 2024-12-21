<template>
  <div class="center q-ma-md">
    <div class="text-center">
      <h3 class="title q-mb-lg q-pb-lg">Vasara</h3>
    </div>
    <q-card class="q-pa-md card content" flat>
      <q-form
        @submit="loginUser"
        class="q-gutter-md"
        @reset="clearForm"
        autofocus
      >
        <div v-if="msg" class="msg">{{ msg }}</div>
        <q-input filled v-model="log" label="Login" />
        <q-input filled v-model="password" label="Password" />
        <div>
          <q-btn
            label="Login"
            type="submit"
            color="primary"
            class="btn send"
            flat
          />
        </div>
      </q-form>
      <div>
        <router-link to="register">
          <p class="guest-account q-mt-sm">No account yet? Register here</p>
        </router-link>
      </div>
      <div class="guest-account q-mt-lg">
        <p>
          Are you a recruiter? Please use guest account and feel free to use the
          app:
        </p>
        <p>login: guest</p>
        <p>password: guest</p>
      </div>
    </q-card>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { login } from "src/services/userservice";
import { useRouter } from "vue-router";

const router = useRouter();

const log = ref("");
const password = ref("");

const msg = ref("");

const loginUser = () => {
  if (!log.value || !password.value) {
    msg.value = "Please fill login and password";
    return;
  }
  login({ login: log.value, password: password.value }).then((response) => {
    if (response instanceof Error) {
      msg.value = `Error! ${response.response.data}`;
    } else {
      msg.value = "";
      router.push("/readAll");
    }
  });
};
</script>

<style scoped>
.card {
  border-radius: 15px;
}

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

.send {
  background-color: gold !important;
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
