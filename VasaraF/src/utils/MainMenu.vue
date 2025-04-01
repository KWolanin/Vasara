<template>
    <q-card flat>
      <header class="row justify-center q-pa-xs">
        <RouterLink to="/" class="q-ma-sm btn row items-center">
          <img
            src="../assets/favicon.ico"
            class="responsive-img"
            width="35"
            height="35"
          />
          <span class="title q-ml-sm inline-flex items-center">Vasara</span>
        </RouterLink>
        <q-space />
        <q-btn class="q-ma-sm btn" flat>
          <RouterLink to="readAll">Read</RouterLink>
        </q-btn>
        <q-btn class="q-ma-sm btn" v-if="isLoggedIn" flat>
          <RouterLink to="mines">My stories</RouterLink>
        </q-btn>
        <q-btn flat class="q-ma-sm btn" color="grey-10">
          <RouterLink to="about">About</RouterLink>
        </q-btn>
        <q-btn v-if="!isLoggedIn" class="q-ma-sm btn" flat color="grey-10">
          <RouterLink to="login">Login</RouterLink>
        </q-btn>
        <q-space />
        <p v-if="isLoggedIn" class="welcome">
          <user-menu :username />
        </p>
      </header>
    </q-card>
</template>


<script setup>
import { computed } from "vue";
import UserMenu from "./UserMenu.vue";
import { useUserStore } from "src/stores/user";

const userStore = useUserStore();
const isLoggedIn = computed(() => !!userStore.id);
const username = computed(() => userStore.username);
</script>

<style scoped>
.btn {
  font-family: "Farro", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: #333 !important;
}

a,
a:visited {
  color: #333;
}

.text-dark {
  color: #333 !important;
}

.bg-dark {
  background: #333 !important;
}

.welcome {
  color: #333;
  font-weight: 800 !important;
  text-transform: uppercase !important;
  margin: 0 !important;
  align-content: center;
}

.title {
  font-family: "WindSong", cursive;
  font-size: 1.3rem;
  color: #333;
  font-weight: 900;
  line-height: 35px;
}


</style>
