<template>
  <main-menu />
  <q-inner-loading :showing="loading">
    Loading...
    <q-spinner-hearts size="50px" color="gold" />
  </q-inner-loading>
  <div>
    <div class="row justify-center q-pa-lg">
      <sort-and-filter @criteria-changed="filterBy" @sort-changed="sortBy"/>
    </div>
    <div v-if="!loading"
      v-for="story in stories"
      :key="story.id"
      class="row justify-center q-pa-lg"
    >
      <story-card :story>
        <template v-slot:following>
          <fav-and-follow :story-id="story.id"/>
        </template>
      </story-card>
    </div>
    <div v-if="!loading && !stories.length" class="not-found">No stories found. Change a criteria and try again</div>
    <div class="row justify-center q-py-lg" v-if="!loading && stories.length">
      <q-pagination
      :model-value=currentPage
      :max="maxPages"
      color="black"
      rounded
      glossy
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
import { fetchStories, count } from "../services/storyservice";
import { onMounted, ref, computed } from "vue";
import StoryCard from "../story/StoryCard.vue";
import MainMenu from "../utils/MainMenu.vue";
import SortAndFilter from "src/utils/SortAndFilter.vue";
import { Story } from "src/types/Story";
import { Criteria } from "../types/Criteria";
import FavAndFollow from "../utils/FavAndFollow.vue";

const stories = ref<Story[]>([]);
const loading = ref<boolean>(true);
const storiesAmount = ref<number>(0)
const storiesPerPage : number = 5
const currentPage = ref<number>(1)
const sortCriteria = ref<string>("updateDt")

let criteria: Criteria = {
  title: "",
  author: "",
  fandoms: [],
  tags: [],
  description: ""
}

onMounted(() => {

  count()
  .then((response) => {
    storiesAmount.value = response;
  })

  fetchStories(1, storiesPerPage, criteria, sortCriteria.value)
    .then((response) => {
      stories.value = response.content;
      loading.value = false;
      storiesAmount.value = response.totalElements
    })
    .catch((error) => {
      console.error(error);
    });
});

const maxPages = computed<number>(() => {
  return Math.ceil(storiesAmount.value / storiesPerPage)
})

const setPage = (newPage: number) => {
  loading.value = true
  fetchStories(newPage, storiesPerPage, criteria, sortCriteria.value)
    .then((response) => {
      stories.value = response.content;
      currentPage.value = newPage;
      storiesAmount.value = response.totalElements
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
    });
}

const filterBy = (crit: Criteria) => {
  loading.value = true
  criteria = crit
  fetchStories(1, storiesPerPage, criteria, sortCriteria.value)
    .then((response) => {
      stories.value = response.content;
      currentPage.value = 1;
      storiesAmount.value = response.totalElements
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
    });
 }

const sortBy = ((type: string) => {
  switch (type) {
  case "Update date (newest)":
    sortCriteria.value = "updateDt";
    break;
  case "Update date (oldest)":
    sortCriteria.value = "updateDt asc";
    break;
  case "Author (A-Z)":
    sortCriteria.value = "author";
    break;
  case "Author (Z-A)":
    sortCriteria.value = "author desc";
    break;
  case "Story title (A-Z)":
    sortCriteria.value = "title";
    break;
    case "Story title (Z-A)":
    sortCriteria.value = "title desc";
    break;
  default:
    sortCriteria.value = "updateDt";
    break;
}
  fetchStories(1, storiesPerPage, criteria, sortCriteria.value)
    .then((response) => {
      stories.value = response.content;
      currentPage.value = 1;
      storiesAmount.value = response.totalElements
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
    });
})


</script>

<style scoped>

.not-found {
  text-align: center;
  color: gray;
  margin-top: 100px;
}

</style>
