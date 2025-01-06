<template>
  <main-menu />
  <q-inner-loading :showing="loading">
    Loading...
    <q-spinner-hearts size="50px" color="amber" />
  </q-inner-loading>
  <div v-if="!loading">
    <div
      v-for="story in stories"
      :key="story.id"
      class="row justify-center q-pa-lg"
    >
      <story-card :story />
    </div>
  </div>
</template>

<script setup lang="ts">
import { fetchStories } from "../services/storyservice";
import { onMounted, ref } from "vue";
import StoryCard from "../story/StoryCard.vue";
import MainMenu from "../utils/MainMenu.vue";
import { Story } from "src/types/Story";

const stories = ref<Story[]>([]);
const loading = ref<boolean>(true);

onMounted(() => {
  fetchStories()
    .then((response) => {
      stories.value = response;
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
    });
});
</script>
