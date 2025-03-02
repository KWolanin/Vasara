<template>
  <main-menu />
  <div class="row justify-center">
    <q-card class="col-10 q-pa-md q-mb-md" flat>
      <form @submit.prevent.stop="saveChapter" class="q-gutter-md">
        <q-btn class="bg-accent-gold btn" flat type="submit">
          {{ route.name === "add" ? "Publish" : "Update" }}
        </q-btn>
        <q-input
          filled
          ref="titleRef"
          v-model="chapterTitle"
          :label="chapterNumber"
          :rules="[(val) => !!val || 'Title is required']"
          class="q-py-md"
          color="burgund"
        />
        <q-editor
          height="70vh"
          v-model="content"
          :dense="$q.screen.lt.md"
          :toolbar="toolbar"
          :fonts="fonts"
        />
      </form>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, Ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { Chapter } from "src/types/Chapter";
import { createChapter, fetchChapter } from "../services/chapterservice";
import { showNotification } from "src/utilsTS/notify";
import { useToolbar } from "src/utilsTS/toolbar";
import { fonts } from "src/utilsTS/fonts";

const toolbar = useToolbar();
const router = useRouter();
const route = useRoute();

const chapterTitle = ref<string | number>("");
const content = ref<string>("");

const titleRef = ref(null);

const props = defineProps<{
  authorId: number;
  storyId: number;
  chapters: number;
}>();

const chapterNumber = computed<string>(() => {
  if (route.name === "edit") return "";
  return `Chapter no. ${props.chapters + 1}`;
});

const saveChapter = (): void => {
  titleRef.value.validate();
  if (titleRef.value.hasError) {
    return;
  }

  if (isHtmlContentEmpty(content.value)) {
    showNotification("Chapter must have a content!", "warning");
    return;
  }

  let chapterO: Chapter | Omit<Chapter, "id">;

  chapterO = createChapterObject();
  createChapter(chapterO)
    .then(() => {

      route.name === "add"
        ? router.push("/mines")
        : router.push({ path: "/manage", query: { storyId: chapterO.storyId } });

      const msg = route.name === "edit" ? "updated" : "published";
      showNotification(`Chapter ${msg} successfully!`, "positive");
    })
    .catch((error: unknown) => {
      console.error(error);
      showNotification(
        "An error occurred while trying to save the chapter. Please try again later.",
        "negative"
      );
    });
};

const existingChapter: Ref<Chapter | null> = ref(null);

onMounted(() => {
  if (route.name === "edit") {
    fetchChapter(Number(route.query.storyId), Number(route.query.chapters))
      .then((chapter) => {
        chapterTitle.value = chapter.chapterTitle;
        content.value = chapter.content;
        existingChapter.value = chapter;
      })
      .catch((error) => {
        console.error(error);
      });
  }
});

function createChapterObject(): Omit<Chapter, "id"> {
  const date = new Date().toISOString();

  const baseChapterData = {
    chapterTitle: chapterTitle.value,
    content: content.value,
    updated: date
  };

  if (route.name === "add") {
    return {
      ...baseChapterData,
      authorId: props.authorId,
      storyId: props.storyId,
      chapterNo: props.chapters + 1,
      published: date,
      storyDTO: null,
    };
  } else {
    return {
      ...baseChapterData,
      ...existingChapter.value
    };
  }
}


/* prevents saving a chapter when it has only html tags, no real text */
function isHtmlContentEmpty(html: string) {
  const div = document.createElement("div");
  div.innerHTML = html;
  return div.innerText.trim().length === 0;
}
</script>
