<template>
  <main-menu />
  <div div class="row justify-center q-pa-lg">
    <q-card class="col-8 q-pa-lg card" >
      <div class="q-ml-md col-4">
        <div class="text-body1">{{ username }}'s profile</div>
        <div class="text-body2">{{ description }}</div>
        <q-btn
          v-if="isLoggedIn"
          push
          :color="followColor"
          flat
          round
          icon="email"
          class="q-pt-md"
          @click="follow"
        >
          <q-tooltip>
            Follow author and get an e-mail when they publish new story
          </q-tooltip>
        </q-btn>
      </div>
      <div class="q-ml-md col-4 q-mt-lg">
        <div class="text-body1">This user creates these stories:</div>
        <story-card
          v-for="story in stories"
          :story="story"
          bordered
          class="q-ma-md"
        >
          <template v-slot:following>
            <fav-and-follow v-if="isLoggedIn" :story-id="story.id" />
          </template>
        </story-card>
      </div>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useRoute } from "vue-router";
import { getAuthor } from "src/services/authorservice";
import { StoryInfo } from "src/types/StoryInfo";
import StoryCard from "src/story/StoryCard.vue";
import { useUserStore } from "src/stores/user";
import FavAndFollow from "../utils/FavAndFollow.vue";
import { addToFollows, isFollow } from "src/services/followauthorservice";
import { showNotification } from "src/utilsTS/notify";

const userStore = useUserStore();
const isLoggedIn = computed(() => !!userStore.id);

const route = useRoute();

const username = ref<string>("");
const stories = ref<StoryInfo[]>([]);
const description = ref<string>("");

onMounted(() => {
  getAuthor(Number(route.query.authorId)).then((author) => {
    username.value = author.username;
    stories.value = author.stories;
    description.value = author.description;
  });
  if (isLoggedIn.value) {
    isFollow(userStore.id, Number(route.query.authorId)).then((data) => {
    isFollowed.value = data;
  });
  }
});

const isFollowed = ref<Boolean>(false);

const followColor = computed(() => {
  return isFollowed.value ? "purple" : "black";
});

const follow = (): void => {
  addToFollows(userStore.id, Number(route.query.authorId))
    .then((data) => {
      const msg = data ? "Author followed" : "Author unfollowed";
      isFollowed.value = data;
      showNotification(msg, data ? "positive" : "negative")
    })
    .catch((err) => {
      console.error("Follow error:", err);
      showNotification(`Error occurred while adding or removing follow: ${err.response.data.message}`, "negative")
    });
};
</script>
