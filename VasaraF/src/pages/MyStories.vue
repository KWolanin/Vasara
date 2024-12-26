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
        <edit-menu :story @story-deleted="reloadStories()" />
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

<style scoped>
.content {
  width: 80%;
}

.btn {
  font-family: "Farro", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: #333 !important;
}

</style>
