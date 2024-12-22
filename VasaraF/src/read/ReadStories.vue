<template>
  <main-menu />
  <q-inner-loading :showing="loading">
    Loading...
    <q-spinner-gears size="50px" color="primary" />
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

<script setup>
import { fetchStories } from "../services/storyservice";
import { onMounted, ref } from "vue";
import StoryCard from "../components/StoryCard.vue";
import MainMenu from "../components/MainMenu.vue";

const stories = ref([]);
const loading = ref(true);

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
