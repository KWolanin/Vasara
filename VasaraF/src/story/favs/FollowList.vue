<template>
  <div class="text-h6">Following stories</div>
  Here is a list of your following stories. You will be receive an email
  everytime one of these stories will be updated.
    <q-inner-loading :showing="loading">
  Loading...
  <q-spinner-hearts size="50px" color="gold" />
</q-inner-loading>
  <div
   v-if="!loading"
    v-for="story in myFollows"
    :key="story.id"
    class="row justify-center q-pa-lg"
  >
    <story-card :story bordered>
      <template v-slot:removeFollowing>
        <!-- todo-->
      </template>
    </story-card>
  </div>

  <div v-if="!loading && !myFollows.length" class="not-found">No follows yet! </div>

  <div class="row justify-center q-py-lg" v-if="!loading && myFollows.length">
    <q-pagination
    v-if="storiesPerPage <= myFollows.length"
      :model-value="currentPage"
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
</template>

<script setup lang="ts">
import { countFollows, findMyFollows } from "src/services/followservice";
import { Story } from "src/types/Story";
import { computed, onMounted, ref } from "vue";
import { useUserStore } from "src/stores/user";
import StoryCard from "../StoryCard.vue";

const store = useUserStore();

const loading = ref<boolean>(true);

const storiesAmount = ref<number>(0);
const storiesPerPage: number = 5;
const currentPage = ref<number>(1);

const myFollows = ref<Story[]>([]);

onMounted(() => {
loading.value = true

countFollows()
  .then((response) =>{
    storiesAmount.value = response
  })

findMyFollows(currentPage.value, storiesPerPage, store.id).then((response) => {
  myFollows.value = response.content;
  loading.value = false;
});
});

const maxPages = computed<number>(() => {
return Math.ceil(storiesAmount.value / storiesPerPage);
});

const setPage = (newPage: number) => {
loading.value = true
findMyFollows(currentPage.value, storiesPerPage, store.id)
  .then((response) => {
    myFollows.value = response.content;
    currentPage.value = newPage;
    loading.value = false;
  })
  .catch((error) => {
    console.error(error);
  });
};
</script>

