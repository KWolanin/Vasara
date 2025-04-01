<template>
  <q-btn class="q-ma-sm btn" flat icon="edit" @click="edit">
    <q-tooltip> Edit story's details </q-tooltip>
  </q-btn>
  <q-btn class="q-ma-sm btn" flat icon="post_add" @click="addChapter">
    <q-tooltip> Add new chapter </q-tooltip>
  </q-btn>
  <q-btn
    class="q-ma-sm btn"
    v-if="story.chaptersNumber > 0"
    flat
    icon="edit_note"
    @click="manageChapters"
  >
    <q-tooltip> Manage chapters </q-tooltip>
  </q-btn>
  <q-btn
    class="q-ma-sm btn"
    flat
    icon="delete_forever"
    color="burgund"
    @click="openDeleteDialog"
  >
    <q-tooltip> Delete forever</q-tooltip>
  </q-btn>

  <q-dialog v-model="deleteDialogVisible" persistent>
    <q-card class="q-ma-lg q-pa-sm">
      <q-card-section class="row items-center">
        <span class="q-ml-sm"
          >Are you sure you want to delete this story? It cannot be undone</span
        >
      </q-card-section>

      <q-card-actions align="right">
        <q-btn
          flat
          label="Cancel"
          color="gray"
          v-close-popup
          @click="deleteDialogVisible = false"
        />
        <q-btn
          flat
          label="Delete anyway"
          color="red"
          v-close-popup
          @click="deleteById(props.story.id)"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { deleteStory } from "../services/storyservice";
import { StoryInfo } from "src/types/StoryInfo";
import { ref } from "vue";

const router = useRouter();

const emit = defineEmits(["storyDeleted"]);

const props = defineProps<{
  story: StoryInfo;
}>();

const addChapter = (): void => {
  router.push({
    path: "/add",
    query: {
      storyId: props.story.id,
      authorId: 1,
      chapters: props.story.chaptersNumber,
    },
  });
};

const deleteDialogVisible = ref<boolean>(false);

const openDeleteDialog = (): void => {
  deleteDialogVisible.value = true;
};

const deleteById = (id: number): void => {
  deleteStory(id)
    .then(() => {
      emit("storyDeleted");
    })
    .catch((error) => {
      console.error(error);
    });
};

const manageChapters = (): void => {
  router.push({ name: "manage", query: { storyId: props.story.id } });
};

const edit = (): void => {
  localStorage.setItem("currentStory", JSON.stringify(props.story));
  router.push({ name: "create" });
};
</script>
