<template>
  <main-menu />
  <div class="row justify-center">
    <q-card class="col-8 q-pa-md card" >
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
        <q-input
          filled
          v-model="title"
          label="Title"
          color="burgund"
          ref="titleRef"
          :rules="[(val) => !!val || 'Title is required']"
        />
        <q-input
          ref="descRef"
          filled
          v-model="description"
          color="burgund"
          maxlength="500"
          counter
          label="Description"
          type="textarea"
          :rules="[(val) => !!val || 'Description is required']"
        />
        <tag-input v-model="tags" label="Tag(s)" />
        <tag-input v-model="fandoms" label="Fandom(s)" />
        <q-select
          v-model="rating"
          :options="ratingOptions"
          label="Rating"
          color="burgund"
          outlined
        />
        <q-checkbox
          v-model="finished"
          label="Mark as completed work"
          color="burgund"
        />
        <q-checkbox
          v-model="comment"
          color="burgund"
          label="Allow comments (registered users)"
        />
        <q-checkbox
          v-model="guestComment"
          color="burgund"
          label="Allow comments (guest users)"
        />

        <div>
          <q-btn
            :label="isEditing ? 'Update' : 'Create'"
            type="submit"
            color="primary"
            class="btn bg-yellow-4"
            flat
          />
          <q-btn
            v-if="!isEditing"
            label="Clear"
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

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { createStory, updateStory } from "../services/storyservice";
import TagInput from "../utils/TagInput.vue";
import { useRouter } from "vue-router";
import { useUserStore } from "../stores/user";
import { Story } from "src/types/Story";
import { StoryInfo } from "src/types/StoryInfo";

const userStore = useUserStore();

const title = ref<string | number>("");
const description = ref<string | number>("");
const fandoms = ref<string[]>([]);
const tags = ref<string[]>([]);
const finished = ref<boolean>(false);
const rating = ref<string>("KIDS");
const comment = ref<boolean>(true);
const guestComment = ref<boolean>(true);

const isEditing = ref<boolean>(false);
const existingStory = ref<Story>(null);

const router = useRouter();

const createNewStory = (): void => {
  const id = userStore.id;
  let story: StoryInfo | Omit<StoryInfo, "id">;
  if (!isEditing.value) {
    story = {
      authorId: id,
      authorName: userStore.username,
      chaptersNumber: 0,
      title: title.value,
      description: description.value,
      tags: tags.value,
      fandoms: fandoms.value,
      finished: finished.value,
      publishDt: new Date(),
      updateDt: new Date(),
      rating: rating.value,
      comment: comment.value,
      guestComment: guestComment.value,
      chaptersTitles: []
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
    story = {
      id: existingStory.value.id,
      authorId: existingStory.value.authorId,
      authorName: existingStory.value.authorName,
      chaptersNumber: existingStory.value.chaptersNumber,
      title: title.value,
      description: description.value,
      tags: tags.value,
      fandoms: fandoms.value,
      finished: finished.value,
      publishDt: existingStory.value.publishDt,
      updateDt: existingStory.value.updateDt,
      rating: rating.value,
      comment: comment.value,
      guestComment: guestComment.value,
      chaptersTitles: []
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

function clearForm(): void {
  title.value = "";
  description.value = "";
  fandoms.value = [];
  tags.value = [];
}

onMounted(() => {
  const storyData = localStorage.getItem("currentStory");

  if (storyData) {
    const story = JSON.parse(storyData);
    title.value = story.title;
    description.value = story.description;
    tags.value = story.tags || [];
    fandoms.value = story.fandoms || [];
    finished.value = story.finished || false;
    isEditing.value = true;
    existingStory.value = story;
    rating.value = story.rating;
    comment.value = story.comment;
    guestComment.value = story.guestComment;
    localStorage.removeItem("currentStory");
  }
});

const ratingOptions = ["KIDS", "TEEN", "ADULT", "MATURE"];
</script>
