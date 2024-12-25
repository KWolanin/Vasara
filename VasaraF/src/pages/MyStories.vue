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
    <div v-if="!stories.length">
      No stories found. Maybe you should add a new story? // wip, link to add
      stories here/
    </div>
  </div>
</template>

<script setup>
import { fetchMyStories } from "../services/storyservice";
import { onMounted, ref } from "vue";
import StoryCard from "../components/StoryCard.vue";
import MainMenu from "../components/MainMenu.vue";
import EditMenu from "../components/EditMenu.vue";
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
