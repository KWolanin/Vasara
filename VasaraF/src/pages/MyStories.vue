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
      <story-card :story>
        <edit-menu :story @story-deleted="reloadStories()" />
      </story-card>
    </div>
  </div>
</template>

<script setup>
import { fetchMyStories } from "../services/storyservice";
import { onMounted, ref } from "vue";
import StoryCard from "./StoryCard.vue";
import MainMenu from "./MainMenu.vue";
import EditMenu from "./EditMenu.vue";
import { Notify } from "quasar";

const stories = ref([]);
const loading = ref(true);

onMounted(() => {
  fetchMyStories()
    .then((response) => {
      stories.value = response;
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
    });
});

const fetchStories = async () => {
  await fetchMyStories()
    .then((response) => {
      stories.value = response;
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
    });
};

const reloadStories = async () => {
  Notify.create({
    message: "Story was deleted!",
    position: "bottom-right",
  });
  await fetchStories();
};
</script>
