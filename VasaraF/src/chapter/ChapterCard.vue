<template>
  <q-card class="q-pa-md" dense flat bordered>
    <p class="text-h6">
      <q-chip size="xs" flat square>
        {{ chapter.chapterNo }}
      </q-chip>
      <div>{{ chapter.chapterTitle }}</div>
    </p>
    <q-space />
    <div class="button-container">
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
        <q-btn
        class="q-mr-sm btn"
        flat
        icon="edit">
          <q-tooltip>Edit chapter title and content</q-tooltip>
        </q-btn>
      </router-link>
      <q-btn
      flat
      icon="delete_forever"
      color="burgund"
      @click="deleteChapter">
      <q-tooltip>Delete forever</q-tooltip>
    </q-btn>
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

<style scoped>
.button-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.button-container > * {
  margin: 0.5rem;
}

</style>
