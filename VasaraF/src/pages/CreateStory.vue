<template>
  <main-menu />
  <div class="row justify-center">
    <q-card class="col-8 q-pa-md">
      <q-card-section>
        <div class="text-h2">Stwórz nową historię</div>
        <div class="text-subtitle2">pierwsze zdanie to dopiero początek</div>
      </q-card-section>
      <q-form
        @submit="createNewStory"
        class="q-gutter-md"
        @reset="clearForm"
        autofocus
      >
        <q-input filled v-model="title" label="Tytuł" />
        <q-input filled v-model="description" label="Opis" type="textarea" />
        <tag-input v-model="tags" />
        <tag-input v-model="fandoms" label="Fandom(y)" />
        <div>
          <q-btn label="Stwórz" type="submit" color="primary" />
          <q-btn
            label="Reset"
            type="reset"
            color="primary"
            flat
            class="q-ml-sm"
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
      console.log(response);
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
