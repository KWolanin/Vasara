<template>
  <q-btn class="q-ma-sm btn" flat icon="edit" @click="edit">
    <q-tooltip> Edit story's details </q-tooltip>
  </q-btn>
  <q-btn class="q-ma-sm btn" flat icon="post_add" @click="addChapter">
    <q-tooltip> Add new chapter </q-tooltip>
  </q-btn>
  <q-btn class="q-ma-sm btn" v-if="story.chaptersNumber > 0" flat icon="edit_note"
    @click="manageChapters">
    <q-tooltip> Manage chapters </q-tooltip>
    </q-btn>
  <q-btn class="q-ma-sm btn" flat icon="delete_forever" color="burgund" @click="deleteById(props.story.id)"
    >    <q-tooltip> Delete forever</q-tooltip>
    </q-btn
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
