<template>
  <q-layout view="hHh lpR fFf">
    <q-page-container>
      <q-page class="flex flex-center">
        <div class="text-center">
          <h1 class="title">Vasara</h1>
          <q-btn class="q-ma-md">
            <RouterLink to="create">
              <h2>Create a story</h2>
            </RouterLink>
          </q-btn>
          <q-btn class="q-ma-md">
            <RouterLink to="readAll">
              <h2>Read stories</h2>
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
}
</style>
