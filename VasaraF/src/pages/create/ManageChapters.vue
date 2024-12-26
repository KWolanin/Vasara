<template>
  <main-menu />
  <div class="row justify-center">
  <q-card class="col-8 q-pa-md card" flat>
    Chapters
  </q-card>
  </div>

  </template>

<script setup lang="ts">
import MainMenu from "../../components/MainMenu.vue";
import { useRoute } from "vue-router";
import { onMounted, ref } from "vue";
const route = useRoute();

const currentStory = ref(0)

onMounted(() => {
  currentStory.value = route.query.params.storyId
})

</script>
