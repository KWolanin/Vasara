<template>
  <q-card class="col-8 q-pa-md">
    <q-chip v-for="(fandom, index) in story.fandoms" :key="index">{{
      fandom
    }}</q-chip>
    <q-chip v-for="(tag, index) in story.tags" :key="index">{{ tag }}</q-chip>
    <div class="text-h2">
      {{ story.title }}
    </div>
    <div class="text-h3">{{ story.authorName }}</div>
    <div><strong>published: </strong> {{ formatDate(story.publishDt) }}</div>
    <span>
      <strong> last update: </strong>{{ formatDate(story.updateDt) }}</span
    >
    <p class="text-h6">
      {{ story.description }}
    </p>
    <q-icon :name="story.finished ? 'check' : 'remove_done'" size="lg" />
    <div class="text-h3">
      {{ story.chaptersNumber }} /
      {{ !story.finished ? "?" : story.chaptersNumber }}
    </div>
    <q-btn @click="readChapter" class="q-my-md">Czytaj</q-btn>
    <slot></slot>
  </q-card>
</template>

<script setup>
import { format } from "date-fns";
import { useRouter } from "vue-router";
const router = useRouter();

const props = defineProps({
  story: {
    type: Object,
    required: true,
  },
});

const formatDate = (date) => {
  return format(new Date(date), "dd.MM.yyyy");
};

const readChapter = () => {
  router.push({
    path: "/read",
    query: {
      storyId: props.story.id,
      chapterNo: 1,
    },
  });
};
</script>
