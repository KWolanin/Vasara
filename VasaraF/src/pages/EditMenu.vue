<template>
  <q-btn class="q-ma-sm bg-primary">Edytuj</q-btn>
  <q-btn class="q-ma-sm bg-secondary" @click="addChapter">Dodaj rozdział</q-btn>
  <q-btn class="q-ma-sm">Zarządzaj rozdziałami</q-btn>
  <q-btn class="q-ma-sm bg-red-4">Skasuj</q-btn>
</template>

<script setup>
import { useRouter } from "vue-router";
const router = useRouter();

const props = defineProps({
  story: {
    type: Object,
    required: true,
  },
});

const addChapter = () => {
  router.push({
    path: "/add",
    query: {
      storyId: props.story.id,
      authorId: 1,
      chapters: props.story.chaptersNumber,
    },
  });
};
</script>
