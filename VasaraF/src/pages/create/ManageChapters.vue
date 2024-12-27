<template>
  <main-menu />
  <div class="row justify-center">
  <q-card class="col-8 q-pa-md card" flat>
    Chapters
    <div v-for="chapter in currentChapters">
      <chapter-card :chapter="chapter" />
    </div>
  </q-card>
  </div>

  </template>

<script setup lang="ts">
import MainMenu from "../../components/MainMenu.vue";
import ChapterCard from "./ChapterCard.vue";
import { useRoute } from "vue-router";
import { onMounted, ref } from "vue";
import { fetchChaptersForStory } from "../../services/chapterservice"

const route = useRoute();

const currentStory = ref(0)
const currentChapters = ref([])

onMounted(() => {
  currentStory.value = Number(route.query.storyId)
  fetchChaptersForStory(currentStory.value).then((chapters) => {
    currentChapters.value = chapters
  })
})

</script>
