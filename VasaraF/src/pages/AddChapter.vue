<template>
  <main-menu />
  <div class="row justify-center">
    <q-card class="col-10 q-pa-md">
      <q-btn @click="saveChapter" class="bg-primary">Publikuj</q-btn>
      <q-input
        filled
        v-model="chapterTitle"
        :label="chapterNumber"
        class="q-py-md"
      />
      <q-editor
        v-smart-quotes
        height="70vh"
        v-model="content"
        :dense="$q.screen.lt.md"
        :toolbar="editorToolbar"
        :fonts="editorFonts"
      />
    </q-card>
  </div>
</template>

<script setup>
import MainMenu from "./MainMenu.vue";
import { ref, computed, watch } from "vue";
import { useQuasar } from "quasar";
import { createChapter } from "../services/chapterservice";
import { useRouter } from "vue-router";

const router = useRouter();

const chapterTitle = ref();
const content = ref("");

const $q = useQuasar();

const props = defineProps({
  authorId: {
    type: Number,
    required: true,
  },
  storyId: {
    type: Number,
    required: true,
  },
  chapters: {
    type: Number,
    required: true,
  },
});

const chapterNumber = computed(() => {
  return `Tytuł rozdziału ${props.chapters + 1}`;
});

const saveChapter = () => {
  const newChapter = {
    chapterTitle: chapterTitle.value,
    content: content.value,
    authorId: props.authorId,
    storyId: props.storyId,
    chapterNo: props.chapters + 1,
  };
  createChapter(newChapter)
    .then((response) => {
      router.push("/mines");
    })
    .catch((error) => {
      console.error(error);
    });
};

const editorToolbar = [
  [
    {
      label: $q.lang.editor.align,
      icon: $q.iconSet.editor.align,
      fixedLabel: true,
      list: "only-icons",
      options: ["left", "center", "right", "justify"],
    },
    {
      label: $q.lang.editor.align,
      icon: $q.iconSet.editor.align,
      fixedLabel: true,
      options: ["left", "center", "right", "justify"],
    },
  ],
  ["bold", "italic", "strike", "underline", "subscript", "superscript"],
  ["token", "hr", "link", "custom_btn"],
  ["print", "fullscreen"],
  [
    {
      label: $q.lang.editor.formatting,
      icon: $q.iconSet.editor.formatting,
      list: "no-icons",
      options: ["p", "h1", "h2", "h3", "h4", "h5", "h6", "code"],
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
