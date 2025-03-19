<template>
  <q-layout view="hHh lpR fFf">
    <q-page-container>
      <q-page class="flex flex-center">
        <div class="text-center">
          <h1 class="title">Vasara</h1>
          <h3 class="subtitle">your summer trip to stories</h3>
          <img
            src="./assets/favicon.ico"
            class="responsive-img"
            width="150"
            height="150"
          />
          <div class="row justify-center q-mt-md">
            <q-btn class="q-ma-md" flat v-if="isLoggedIn">
              <RouterLink to="create">
                <h2 class="btn">Create</h2>
              </RouterLink>
            </q-btn>
            <q-btn class="q-ma-md" flat>
              <RouterLink to="readAll">
                <h2 class="btn">Read</h2>
              </RouterLink>
            </q-btn>
          </div>
        </div>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script setup>
import { RouterLink } from "vue-router";
import { useUserStore } from "src/stores/user";
import { computed } from "vue";

const userStore = useUserStore();
const isLoggedIn = computed(() => !!userStore.id);
</script>

<style scoped>
.title {
  font-family: "WindSong", cursive;
  font-size: 10rem;
  text-align: center;
  color: #333;
  margin-bottom: 0;
  margin-top: 0;
}

.subtitle {
  font-family: "Farro", sans-serif;
  font-size: 1.5rem;
  text-align: center;
  font-weight: 400;
  color: gold;
}

.btn {
  font-size: 3rem;
  text-transform: lowercase;
  font-family: "Farro", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: #333;
}

.responsive-img {
  max-width: 100%;
  height: auto;
}

@media (max-width: 768px) {
  .title {
    font-size: 6rem;
  }

  .subtitle {
    font-size: 1.2rem;
  }

  .btn {
    font-size: 2rem;
  }
}

@media (max-width: 480px) {
  .title {
    font-size: 4rem;
  }

  .subtitle {
    font-size: 1rem;
  }

  .btn {
    font-size: 1.5rem;
  }

  .q-btn {
    width: 100%;
  }
}
</style>
