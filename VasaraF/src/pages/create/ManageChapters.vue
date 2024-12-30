<template>
  <main-menu />
  <div class="row justify-center">
  <q-card class="col-8 q-pa-md card" flat>
    <p class="text-h4">Manage chapters</p>
    <p class="body-1">Drag and drop to change chapter's order. Edit its content or rename it.</p>
    <VueDraggable
    v-model="currentChapters"
    @end="changeOrder"
    >
      <div v-for="chapter in currentChapters" :key="chapter.chapterNo">
        <chapter-card :chapter="chapter" class="q-mt-md"/>
      </div>
    </VueDraggable>
    <q-space/>
    <q-btn class="q-mt-md" @click="saveChanges" flat>Save</q-btn>
  </q-card>
  </div>
  </template>

<script setup lang="ts">
import MainMenu from "../../components/MainMenu.vue";
import { useRoute } from "vue-router";
import { onMounted, ref } from "vue";
import { fetchChaptersForStory, updateChaptersOrder } from "../../services/chapterservice"
import { VueDraggable } from 'vue-draggable-plus'
import ChapterCard from "../create/ChapterCard.vue"
import { Chapter } from "../../types/Chapter"
import { Notify } from "quasar";


const route = useRoute();

const currentStory = ref(0)
const currentChapters = ref<Chapter[]>([])

onMounted(() => {
  currentStory.value = Number(route.query.storyId)
  fetchChaptersForStory(currentStory.value).then((chapters) => {
    currentChapters.value = chapters
    console.log(currentChapters.value)
  })
})

const changeOrder = () => {
  currentChapters.value.forEach((chapter, index) => {
    chapter.chapterNo = index + 1
  })
}

const saveChanges = () => {
  updateChaptersOrder(currentChapters.value).then(() => {
    Notify.create({
    message: "Chapters order was changed!",
    position: "bottom-right",
    });
    console.log(currentChapters.value)
  }).catch((err) => {
    console.error(err)
    Notify.create({
    message: "Something went wrong!",
    position: "bottom-right",
    });
  })
}

</script>
