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
      <story-card :story>
        <edit-story-menu :story @story-deleted="reloadStories()" />
      </story-card>
    </div>
    <div class="row justify-center" v-if="!stories.length">
      <q-card class="q-pa-md card content card" flat>
        <q-card-title>No Stories Found</q-card-title>
        <p>
          Maybe you should add a new story?
        </p>
        <Router-link to="create">
          <q-btn class="q-ml-md btn" label="+ Add" flat />
        </Router-link>
      </q-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { fetchMyStories } from "../services/storyservice";
import { onMounted, ref } from "vue";
import StoryCard from "../story/StoryCard.vue";
import MainMenu from "../utils/MainMenu.vue";
import EditStoryMenu from "../story/EditStoryMenu.vue";
import { Notify } from "quasar";
import { Story } from "src/types/Story";

const stories = ref<Story[]>([]);
const loading = ref<boolean>(true);

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

<style scoped>
.content {
  width: 80%;
}

</style>
