<template>
  <q-card class="q-pa-md" dense>
    <p class="text-h6">
      <q-chip size="xs" flat square>
        {{ chapter.chapterNo }}
      </q-chip>
      <q-input v-model="chapter.chapterTitle" outlined />
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
import { useUserStore } from "../../stores/user";
const userStore = useUserStore();

const props = defineProps({
  chapter: {
    type: Object,
    required: true,
  },
});

const deleteChapter = () => {
  emit("delete-chapter", props.chapter.id);
};
</script>

<style scoped>
.btn {
  font-family: "Farro", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: #333;
}

a:visited {
  color: #333;
}
</style>
