<template>
  <q-card class="q-pa-md" dense>
    <p class="text-h6">
      <q-chip size="xs" flat square>
        {{ chapter.chapterNo }}
      </q-chip>
      <div>{{ chapter.chapterTitle }}</div>
        <!-- <q-input v-model="chapter.chapterTitle" outlined disable/> -->
    </p>
    <q-space />
    <div style="display: flex; justify-content: flex-end">
      <router-link
        :to="{
          name: 'edit',
          query: {
            storyId: chapter.storyId,
            authorId: userStore.id,
            chapters: chapter.chapterNo,
          },
        }"
      >
        <q-btn class="q-mr-sm btn" flat>Edit</q-btn>
      </router-link>
      <q-btn flat class="del" @click="deleteChapter">Delete</q-btn>
    </div>
  </q-card>
</template>

<script setup lang="ts">
const emit = defineEmits();
import { Chapter } from "src/types/Chapter";
import { useUserStore } from "../stores/user";
const userStore = useUserStore();

const props = defineProps<{
  chapter: Chapter;
}>();

const deleteChapter = () : void => {
  emit("delete-chapter", props.chapter.id);
};
</script>
