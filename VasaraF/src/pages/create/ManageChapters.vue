<template>
  <main-menu />
  <div class="row justify-center">
  <q-card class="col-8 q-pa-md card" flat>
    <p class="text-h4">Manage chapters</p>
    <p class="body-1">Drag and drop to change chapter's order. Edit its content or rename it. If you want to edit chapter's content save other changes first.</p>
    <VueDraggable
    v-model="currentChapters"
    @end="changeOrder"
    >
      <div v-for="chapter in currentChapters" :key="chapter.chapterNo">
        <chapter-card
         :chapter="chapter"
         class="q-mt-md"
         @delete-chapter="openDeleteDialog"
         />
      </div>
    </VueDraggable>
    <q-space/>
    <q-btn class="q-mt-md save" @click="saveChanges" flat>Save order</q-btn>
  </q-card>
  </div>

  <q-dialog v-model="deleteDialogVisible" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <span class="q-ml-sm">Are you sure you want to delete this chapter? It cannot be undone</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancel" color="gray" v-close-popup @click="deleteDialogVisible  = false" />
          <q-btn flat label="Delete anyway" color="red" v-close-popup @click="deleteChapter"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </template>

<script setup lang="ts">
import MainMenu from "../../components/MainMenu.vue";
import { useRoute } from "vue-router";
import { onMounted, ref } from "vue";
import { fetchChaptersForStory, updateChaptersOrder, deleteChapterFromDb } from "../../services/chapterservice"
import { VueDraggable } from 'vue-draggable-plus'
import ChapterCard from "../create/ChapterCard.vue"
import { Chapter } from "../../types/Chapter"
import { Notify } from "quasar";


const route = useRoute();

const currentStory = ref(0)
const currentChapters = ref<Chapter[]>([])


const deleteDialogVisible = ref(false);
const chapterToDelete = ref(0)

onMounted(() => {
  currentStory.value = Number(route.query.storyId)
  fetchChaptersForStory(currentStory.value).then((chapters) => {
    chapters.sort((a, b) => a.chapterNo - b.chapterNo)
    currentChapters.value = chapters
  })
})

const changeOrder = () => {
  currentChapters.value.forEach((chapter, index) => {
    chapter.chapterNo = index + 1
  })
}

const saveChanges = () => {
  updateChaptersOrder(currentChapters.value).then(() => {
    Notify.create({
    message: "Chapters order was changed!",
    position: "bottom-right",
    });
  }).catch((err) => {
    console.error(err)
    Notify.create({
    message: "Something went wrong!",
    position: "bottom-right",
    });
  })
}

const openDeleteDialog = (id: number) => {
  deleteDialogVisible.value = true;
  chapterToDelete.value = id;
};

const deleteChapter = () => {
  if (chapterToDelete.value > 0) {
    const id = chapterToDelete.value;
    deleteChapterFromDb(id).then((response) => {
      if (response) {
        currentChapters.value = currentChapters.value.filter(c => c.id !== id);
        currentChapters.value.forEach((chapter, idx) => {
          chapter.chapterNo = idx + 1;
        });
        Notify.create({
          message: "Chapter has been deleted!",
          position: "bottom-right",
        });
      } else {
        Notify.create({
          message: "Something went wrong!",
          position: "bottom-right",
        });
      }
    })
    .catch((err) => {
      console.error(err);
      Notify.create({
        message: "Something went wrong!",
        position: "bottom-right",
      });
    });
  }
}



</script>

<style scoped>
.save {
  background-color: gold !important;
}

</style>
