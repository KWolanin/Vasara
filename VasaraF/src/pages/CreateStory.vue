<template>
  <main-menu />
  <div class="row justify-center">
    <q-card class="col-8 q-pa-md card" flat>
      <q-card-section>
        <div v-if="!isEditing" class="text-h2">Create a new story</div>
        <div v-else class="text-h2">Edit the story</div>
        <div v-if="!isEditing" class="text-subtitle2">
          First step into an adventure
        </div>
        <div v-else class="text-subtitle2">Just a small fixes</div>
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
          <q-btn
            :label="isEditing ? 'Update' : 'Create'"
            type="submit"
            color="primary"
            class="btn send"
            flat
          />
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
import { ref, onMounted } from "vue";
import { createStory, updateStory } from "../services/storyservice";
import TagInput from "./TagInput.vue";
import MainMenu from "./MainMenu.vue";
import { useRouter } from "vue-router";
import { useUserStore } from "../stores/user";

const userStore = useUserStore();

const title = ref("");
const description = ref("");
const fandoms = ref([]);
const tags = ref([]);

const isEditing = ref(false);
const existingStory = ref({});

const router = useRouter();

// function updateTags(newTags) {
//   tags.value = newTags;
// }

// function updateFandoms(newFandoms) {
//   fandoms.value = newFandoms;
// }

const createNewStory = () => {
  const id = userStore.id;
  if (!isEditing.value) {
    const story = {
      authorId: id,
      title: title.value,
      description: description.value,
      tags: tags.value,
      fandoms: fandoms.value,
      finished: false,
      publishDt: new Date(),
      updateDt: new Date(),
    };

    createStory(story)
      .then(() => {
        clearForm();
        router.push("/mines");
      })
      .catch((error) => {
        console.error(error);
      });
  } else {
    const story = {
      id: existingStory.value.id,
      authorId: existingStory.value.authorId,
      title: title.value,
      description: description.value,
      tags: tags.value,
      fandoms: fandoms.value,
      finished: false,
      publishDt: existingStory.value.publishDt,
      updateDt: existingStory.value.updateDt,
    };
    updateStory(story)
      .then(() => {
        clearForm();
        router.push("/mines");
      })
      .catch((error) => {
        console.error(error);
      });
  }
};

function clearForm() {
  title.value = "";
  description.value = "";
  fandoms.value = [];
  tags.value = [];
}

onMounted(() => {
  console.log(localStorage.getItem("user"));
  const storyData = localStorage.getItem("currentStory");

  if (storyData) {
    const story = JSON.parse(storyData);
    title.value = story.title;
    description.value = story.description;
    tags.value = story.tags || [];
    fandoms.value = story.fandoms || [];

    isEditing.value = true;
    existingStory.value = story;
    localStorage.removeItem("currentStory");
  }
});
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

.card {
  border-radius: 15px;
}

.send {
  background-color: gold !important;
}
</style>
