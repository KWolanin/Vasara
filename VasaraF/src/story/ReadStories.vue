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
    <div class="row justify-center q-py-lg">
      <q-pagination
      :model-value=currentPage
      :max="maxPages"
      color="black"
      rounded
      glossy
      active-color="amber"
      direction-links
      boundary-links
      icon-first="skip_previous"
      icon-last="skip_next"
      icon-prev="fast_rewind"
      icon-next="fast_forward"
      @update:model-value="setPage"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { fetchStories, count } from "../services/storyservice";
import { onMounted, ref, computed } from "vue";
import StoryCard from "../story/StoryCard.vue";
import MainMenu from "../utils/MainMenu.vue";
import { Story } from "src/types/Story";

const stories = ref<Story[]>([]);
const loading = ref<boolean>(true);
const storiesAmount = ref<number>(0)
const storiesPerPage : number = 5
const currentPage = ref<number>(1)

onMounted(() => {

  count()
  .then((response) => {
    storiesAmount.value = response;
  })

  fetchStories(1, storiesPerPage)
    .then((response) => {
      stories.value = response.content;
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
    });
});

const maxPages = computed<number>(() => {
  return Math.ceil(storiesAmount.value / storiesPerPage)
})

const setPage = (newPage: number) => {
  fetchStories(newPage, storiesPerPage)
    .then((response) => {
      stories.value = response.content;
      currentPage.value = newPage;
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
    });
}


</script>
