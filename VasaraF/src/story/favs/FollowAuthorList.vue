<template>
  <div class="text-h6">Following authors</div>
  Here is a list of authors you follows. You will be receive an email everytime
  one of them publish a new story.
  <q-inner-loading :showing="loading">
    Loading...
    <q-spinner-hearts size="50px" color="gold" />
  </q-inner-loading>
  <div
    v-if="!loading"
    v-for="author in myFollows"
    :key="author.authorId"
    class="row justify-center q-mt-md"
  >
    <author-card :username="author.authorUsername" :id="author.authorId">
    </author-card>
  </div>

  <div v-if="!loading && !myFollows.length" class="not-found">
    No follows yet!
  </div>
  <div class="row justify-center q-py-lg" v-if="!loading && myFollows.length">
    <q-pagination
      v-if="myFollows.length"
      :model-value="currentPage"
      :max="maxPages"
      color="black"
      rounded
      active-color="burgund"
      active-design="unelevated"
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
import {
  countFollowedAuthors,
  findMyFollowedAuthors,
} from "src/services/followauthorservice";
import { computed, onMounted, ref } from "vue";
import { useUserStore } from "src/stores/user";
import { AuthorInfo } from "src/types/AuthorInfo";
import AuthorCard from "src/users/AuthorCard.vue";

const store = useUserStore();
const loading = ref<boolean>(true);

const storiesAmount = ref<number>(0);
const storiesPerPage: number = 5;
const currentPage = ref<number>(1);

const myFollows = ref<AuthorInfo[]>([]);

onMounted(() => {
  loading.value = true;

  countFollowedAuthors().then((response) => {
    storiesAmount.value = response;
  });

  findMyFollowedAuthors(currentPage.value, storiesPerPage, store.id).then(
    (response) => {
      myFollows.value = response.content;
      loading.value = false;
    }
  );
});

const maxPages = computed<number>(() => {
  return Math.ceil(storiesAmount.value / storiesPerPage);
});

const setPage = (newPage: number) => {
  loading.value = true;
  findMyFollowedAuthors(newPage, storiesPerPage, store.id)
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
