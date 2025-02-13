<template>
  <main-menu/>
  <div div class="row justify-center q-pa-lg">
    <q-card class="col-8 q-pa-lg card" flat>
    <div class="q-ml-md col-4">
      <div class="text-body1">{{ username }}'s profile</div>
      <div class="text-body2">{{description}}</div>
      </div>
      <div class="q-ml-md col-4 q-mt-lg">
      <div class="text-body1">This user creates these stories: </div>
        <story-card v-for="story in stories" :story="story"bordered class="q-ma-md">
          <template v-slot:following>
          <fav-and-follow v-if="isLoggedIn" :story-id="story.id"/>
        </template>
        </story-card>
      </div>
  </q-card>
  </div>

  </template>

  <script setup lang="ts">
  import MainMenu from "../utils/MainMenu.vue";
  import { ref, onMounted, computed } from "vue";
  import { useRoute } from "vue-router";
  import { getAuthor } from "src/services/authorservice";
import { Story } from "src/types/Story";
import StoryCard from "src/story/StoryCard.vue";
import { useUserStore } from "src/stores/user";
import FavAndFollow from "../utils/FavAndFollow.vue";

const userStore = useUserStore();
const isLoggedIn = computed(() => !!userStore.id);

const route = useRoute();

const username = ref<string>("");
const stories = ref<Story[]>([]);
const description = ref<string>("");

onMounted(() => {
getAuthor(Number(route.query.authorId)).then((author) => {
  username.value = author.username;
  stories.value = author.stories;
  description.value = author.description;
});
})
  </script>
