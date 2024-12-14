<template>
  <main-menu />
  <div class="row justify-center">
    <q-card class="col-8 q-pa-md">
      <q-card-section>
        <div class="text-h2">Create a new story</div>
        <div class="text-subtitle2">first step into an adventure</div>
      </q-card-section>
      <q-form
        @submit="createNewStory"
        class="q-gutter-md"
        @reset="clearForm"
        autofocus
      >
        <q-input filled v-model="title" label="Title" />
        <q-input
          filled
          v-model="description"
          label="Description"
          type="textarea"
        />
        <tag-input v-model="tags" label="Tag(s)" />
        <tag-input v-model="fandoms" label="Fandom(s)" />
        <div>
          <q-btn label="Create" type="submit" color="primary" class="btn" />
          <q-btn
            label="Reset"
            type="reset"
            color="primary"
            flat
            class="q-ml-sm btn"
          />
        </div>
      </q-form>
    </q-card>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { createStory } from "../services/storyservice";
import TagInput from "./TagInput.vue";
import MainMenu from "./MainMenu.vue";
import { useRouter } from "vue-router";

const title = ref("");
const description = ref("");
const fandoms = ref([]);
const tags = ref([]);

const router = useRouter();

function updateTags(newTags) {
  tags.value = newTags;
}

function updateFandoms(newFandoms) {
  fandoms.value = newFandoms;
}

const createNewStory = () => {
  const story = {
    authorId: 1,
    title: title.value,
    description: description.value,
    tags: tags.value,
    fandoms: fandoms.value,
    finished: false,
    publishDt: new Date(),
    updateDt: new Date(),
  };

  createStory(story)
    .then((response) => {
      clearForm();
      router.push("/mines");
    })
    .catch((error) => {
      console.error(error);
    });
};

function clearForm() {
  title.value = "";
  description.value = "";
  fandoms.value = [];
  tags.value = [];
}
</script>

<style scoped>
.btn {
  font-family: "Farro", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: #333 !important;
}

a:visited {
  color: #333;
}
</style>
