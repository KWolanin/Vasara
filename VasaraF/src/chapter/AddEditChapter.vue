<template>
  <main-menu />
  <div class="row justify-center">
    <q-card class="col-10 q-pa-md q-mb-md">
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
        />
        <q-editor
          height="70vh"
          v-model="content"
          :dense="$q.screen.lt.md"
          :toolbar="editorToolbar"
          :fonts="editorFonts"
        />
      </form>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import MainMenu from "../utils/MainMenu.vue";
import { ref, computed, onMounted, Ref } from "vue";
import { useQuasar } from "quasar";
import { createChapter, fetchChapter } from "../services/chapterservice";
import { useRouter, useRoute } from "vue-router";
import { Chapter } from "src/types/Chapter";
import { Notify } from "quasar";

const router = useRouter();
const route = useRoute();
const $q = useQuasar();

const chapterTitle = ref<string>("");
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

function isHtmlContentEmpty(html) {
  const div = document.createElement("div");
  div.innerHTML = html;
  return div.innerText.trim().length === 0;
}

const saveChapter = (): void => {
  titleRef.value.validate();
  if (titleRef.value.hasError) {
    return;
  }

  if (isHtmlContentEmpty(content.value)) {
    Notify.create({
      message: "Chapter must have a content!",
      position: "bottom-right",
      type: "warning",
    });
    return;
  }
  let c: Chapter | Omit<Chapter, "id">;
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
    };
  } else {
    c = {
      ...existingChapter.value,
      chapterTitle: chapterTitle.value,
      content: content.value,
      updated: date,
    };
  }
  createChapter(c)
    .then(() => {
      route.name === "add"
        ? router.push("/mines")
        : router.push({ path: "/manage", query: { storyId: c.storyId } });
      const msg = route.name === "edit" ? "updated" : "published";
      Notify.create({
        message: `Chapter ${msg} successfully!`,
        position: "bottom-right",
        type: "positive",
      });
    })
    .catch((error: unknown) => {
      console.error(error);
      Notify.create({
        message: `Error occurred!`,
        position: "bottom-right",
        type: "negative",
      });
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

const editorToolbar = [
  [
    {
      label: $q.lang.editor.align,
      icon: $q.iconSet.editor.align,
      fixedLabel: true,
      list: "only-icons",
      options: ["left", "center", "right", "justify"],
    },
  ],
  ["bold", "italic", "strike", "underline", "subscript", "superscript"],
  ["token", "hr", "link", "custom_btn"],
  ["fullscreen"],
  [
    {
      label: $q.lang.editor.formatting,
      icon: $q.iconSet.editor.formatting,
      list: "no-icons",
      options: ["p", "h1", "h2", "h3", "h4", "h5", "h6"],
    },
    {
      label: $q.lang.editor.fontSize,
      icon: $q.iconSet.editor.fontSize,
      fixedLabel: true,
      fixedIcon: true,
      list: "no-icons",
      options: [
        "size-1",
        "size-2",
        "size-3",
        "size-4",
        "size-5",
        "size-6",
        "size-7",
      ],
    },
    {
      label: $q.lang.editor.defaultFont,
      icon: $q.iconSet.editor.font,
      fixedIcon: true,
      list: "no-icons",
      options: [
        "default_font",
        "arial",
        "arial_black",
        "comic_sans",
        "courier_new",
        "impact",
        "lucida_grande",
        "times_new_roman",
        "verdana",
      ],
    },
    "removeFormat",
  ],
  ["quote", "unordered", "ordered", "outdent", "indent"],

  ["undo", "redo"],
  ["viewsource"],
];

const editorFonts = {
  arial: "Arial",
  arial_black: "Arial Black",
  comic_sans: "Comic Sans MS",
  courier_new: "Courier New",
  impact: "Impact",
  lucida_grande: "Lucida Grande",
  times_new_roman: "Times New Roman",
  verdana: "Verdana",
};
</script>
