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
        <q-toggle v-model="darkEditorMode" color="burgund" label="Dark/Light editor mode" />
        <q-editor
          height="70vh"
          v-model="content"
          :dense="$q.screen.lt.md"
          :toolbar="toolbar"
          :fonts="fonts"
          :dark="darkEditorMode"
        />
      </form>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, Ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { Chapter } from "src/types/Chapter";
import {
  createChapter,
  editChapter,
  fetchChapter,
} from "../services/chapterservice";
import { showNotification } from "src/utilsTS/notify";
import { useToolbar } from "src/utilsTS/toolbar";
import { fonts } from "src/utilsTS/fonts";

const toolbar = useToolbar();
const router = useRouter();
const route = useRoute();

const chapterTitle = ref<string | number>("");
const content = ref<string>("");

const darkEditorMode = ref<boolean>(false)

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

  content.value = wrapFirstTextInParagraph(content.value);

  // convert dumb quotes to smart (typographic) quotes (e.g. 'xx' -> ‘xx’) for English and Polish;
  // content.value = convertToSmartQuotesPL(content.value);

  let chapterO: Chapter | Omit<Chapter, "id">;

  chapterO = createChapterObject();

  if (route.name == "add") {
    createChapter(chapterO)
      .then(() => {
        router.push("/mines");
        showNotification(`Chapter published successfully!`, "positive");
      })
      .catch((error: unknown) => {
        console.error(error);
        showNotification(
          "An error occurred while trying to save the chapter. Please try again later.",
          "negative"
        );
      });
  } else {
    editChapter(chapterO)
      .then(() => {
        router.push({
          path: "/manage",
          query: { storyId: chapterO.storyId },
        });
        showNotification(`Chapter updated successfully!`, "positive");
      })
      .catch((error: unknown) => {
        console.error(error);
        showNotification(
          "An error occurred while trying to save the chapter. Please try again later.",
          "negative"
        );
      });
  }
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
  let c = null;
  const date = new Date().toISOString();
  if (route.name === "add") {
    c = {
      chapterTitle: chapterTitle.value,
      content: content.value,
      authorId: props.authorId,
      storyId: props.storyId,
      chapterNo: props.chapters + 1,
      published: date,
      updated: date,
      storyDTO: null,
    };
  } else {
    c = {
      ...existingChapter.value,
      chapterTitle: chapterTitle.value,
      content: content.value,
      updated: date,
    };
  }
  return c;
}

/* prevents saving a chapter when it has only html tags, no real text */
function isHtmlContentEmpty(html: string) {
  const div = document.createElement("div");
  div.innerHTML = html;
  return div.innerText.trim().length === 0;
}

function wrapFirstTextInParagraph(html: string): string {
  return html.replace(/^([^<]+)(?=<|$)/, (match, firstText) => {
    return firstText.trim() ? `<div>${firstText.trim()}</div>` : match;
  });
}

function convertToSmartQuotesEN(text: string) : string {
  return text
    .replace(/(^|[^\\])'([^']+)'/g, '$1\u2018$2\u2019')
    .replace(/(^|[^\\])"([^"]+)"/g, '$1\u201c$2\u201d');
}

function convertToSmartQuotesPL(text: string) : string {
  return text
  .replace(/(^|[^\\])'([^']+)'/g, '$1\u201E$2\u201D')
  .replace(/(^|[^\\])"([^"]+)"/g, '$1\u201E$2\u201D');
}

</script>
