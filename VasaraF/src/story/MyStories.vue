<template>
  <main-menu />
  <div class="row justify-center q-ml-md q-mb-sm">
    <div class="col-8 flex justify-center">
      <q-btn class="q-ma-sm q-mt-xl btn q-pa-md btn" unelevated  color="pink" icon="add_circle_outline"
    @click="addNew"> ADD </q-btn>
    </div>
  </div>

  <q-inner-loading :showing="loading">
    Loading...
    <q-spinner-hearts size="50px" color="gold" />
  </q-inner-loading>
  <div v-if="!loading">
    <div
      v-for="story in stories"
      :key="story.id"
      class="row justify-center q-pa-lg"
    >
      <story-card :story>
        <template v-slot:tools>
          <edit-story-menu :story @story-deleted="reloadStories()" />
        </template>
      </story-card>
    </div>
    <div class="row justify-center" v-if="!stories.length">
      <q-card class="q-pa-md card content card" flat>
        <p>
          No Stories Found
        </p>
        <p>
          Maybe you should add a new story?
        </p>
      </q-card>
    </div>
    <div class="row justify-center q-py-lg">
      <q-pagination
      v-if="stories.length"
      :model-value=currentPage
      :max="maxPages"
      color="black"
      rounded
      active-color="gold"
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
import { fetchMyStories, countMines } from "../services/storyservice";
import { onMounted, ref, computed } from "vue";
import StoryCard from "../story/StoryCard.vue";
import MainMenu from "../utils/MainMenu.vue";
import EditStoryMenu from "../story/EditStoryMenu.vue";
import { Notify } from "quasar";
import { Story } from "src/types/Story";
import { useRouter } from "vue-router";


const router = useRouter();
const stories = ref<Story[]>([]);
const loading = ref<boolean>(true);

const storiesAmount = ref<number>(0)
const storiesPerPage : number = 5
const currentPage = ref<number>(1)

onMounted(() => {

  countMines()
  .then((response) =>{
    storiesAmount.value = response
  })

  fetchMyStories(currentPage.value, storiesPerPage)
    .then((response) => {
      stories.value = response.content;
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
    });
});

const fetchStories = async () => {
  await fetchMyStories(currentPage.value, storiesPerPage)
    .then((response) => {
      stories.value = response.content;
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
    type: "positive"
  });
  await fetchStories();
};


const maxPages = computed<number>(() => {
  return Math.ceil(storiesAmount.value / storiesPerPage)
})

const setPage = (newPage: number) => {
  fetchMyStories(newPage, storiesPerPage)
    .then((response) => {
      stories.value = response.content;
      currentPage.value = newPage;
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
    });
}

const addNew = () => {
  router.push({ name: "create" });
};


</script>

<style scoped>
.content {
  width: 80%;
}

</style>
