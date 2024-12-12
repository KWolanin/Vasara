<template>
  <q-layout view="hHh lpR fFf">
    <q-page-container>
      <q-page class="flex flex-center">
        <div class="text-center">
          <h1 class="title">Vasara</h1>
          <h3 class="subtitle">your summer trip to stories</h3>
          <div>
            <img src="/public/favicon.ico" width="150" height="150" />
          </div>
          <q-btn class="q-ma-md" flat>
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
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script setup>
import { RouterLink } from "vue-router";
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
</style>
