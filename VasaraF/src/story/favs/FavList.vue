<template>

    <div class="text-h6">Favorite stories</div>
    Here is a list of your favorite stories, have fun with them!
    <q-inner-loading :showing="loading">
    Loading...
    <q-spinner-hearts size="50px" color="gold" />
  </q-inner-loading>
    <div
     v-if="!loading"
      v-for="story in myFavs"
      :key="story.id"
      class="row justify-center q-pa-lg"
    >
      <story-card :story bordered>
        <template v-slot:removeFollowing>
          <!-- todo-->
        </template>
      </story-card>
    </div>

    <div v-if="!loading && !myFavs.length" class="not-found">No favorites yet! </div>
    <div class="row justify-center q-py-lg" v-if="!loading && myFavs.length">
      <q-pagination
      v-if="storiesPerPage <= myFavs.length"
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
import { findMyFavs } from "src/services/favoriteservice";
import { Story } from "src/types/Story";
import { computed, onMounted, ref } from "vue";
import { useUserStore } from "src/stores/user";
import StoryCard from "../StoryCard.vue";
import { countFavs } from "src/services/favoriteservice";

const store = useUserStore();

const loading = ref<boolean>(true);

const storiesAmount = ref<number>(0);
const storiesPerPage: number = 5;
const currentPage = ref<number>(1);

const myFavs = ref<Story[]>([]);

onMounted(() => {
  loading.value = true

  countFavs()
  .then((response) =>{
    storiesAmount.value = response
  })

  findMyFavs(currentPage.value, storiesPerPage, store.id).then((response) => {
    myFavs.value = response.content;
    loading.value = false;
  });
});

const maxPages = computed<number>(() => {
  return Math.ceil(storiesAmount.value / storiesPerPage);
});

const setPage = (newPage: number) => {
  loading.value = true
  findMyFavs(currentPage.value, storiesPerPage, store.id)
    .then((response) => {
      myFavs.value = response.content;
      currentPage.value = newPage;
      loading.value = false;
    })
    .catch((error) => {
      console.error(error);
    });
};
</script>

