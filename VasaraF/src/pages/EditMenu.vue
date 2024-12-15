<template>
  <q-btn class="q-ma-sm btn" flat @click="edit">Edit</q-btn>
  <q-btn class="q-ma-sm btn" flat @click="addChapter">Add chapter</q-btn>
  <q-btn class="q-ma-sm btn" flat>Manage chapters</q-btn>
  <q-btn class="q-ma-sm btn del" flat @click="deleteById(props.story.id)"
    >Delete</q-btn
  >
</template>

<script setup>
import { useRouter } from "vue-router";
import { deleteStory } from "../services/storyservice";

const router = useRouter();

const emit = defineEmits(["storyDeleted"]);

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

const deleteById = (id) => {
  deleteStory(id)
    .then(() => {
      emit("storyDeleted");
    })
    .catch((error) => {
      console.error(error);
    });
};

const edit = () => {
  localStorage.setItem("currentStory", JSON.stringify(props.story));
  router.push({ name: "create" });
};
</script>

<style scoped>
.btn {
  font-family: "Farro", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: #333 !important;
}

.del {
  background-color: #aa4465;
  color: #eaeaea !important;
}

a:visited {
  color: #333;
}
</style>
