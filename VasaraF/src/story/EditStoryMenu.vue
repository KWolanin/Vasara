<template>
  <q-btn class="q-ma-sm btn" flat @click="edit">Edit</q-btn>
  <q-btn class="q-ma-sm btn" flat @click="addChapter">Add chapter</q-btn>
  <q-btn class="q-ma-sm btn" v-if="story.chaptersNumber > 0" flat @click="manageChapters"
    >Manage chapters</q-btn
  >
  <q-btn class="q-ma-sm btn del" flat @click="deleteById(props.story.id)"
    >Delete</q-btn
  >
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { deleteStory } from "../services/storyservice";
import { Story } from "src/types/Story";

const router = useRouter();

const emit = defineEmits(["storyDeleted"]);

const props = defineProps<{
  story: Story;
}>();

const addChapter = () : void => {
  router.push({
    path: "/add",
    query: {
      storyId: props.story.id,
      authorId: 1,
      chapters: props.story.chaptersNumber,
    },
  });
};

const deleteById = (id : number) : void => {
  deleteStory(id)
    .then(() => {
      emit("storyDeleted");
    })
    .catch((error) => {
      console.error(error);
    });
};

const manageChapters = () : void => {
  router.push({ name: "manage", query: { storyId: props.story.id } });
};

const edit = () : void => {
  localStorage.setItem("currentStory", JSON.stringify(props.story));
  router.push({ name: "create" });
};
</script>
