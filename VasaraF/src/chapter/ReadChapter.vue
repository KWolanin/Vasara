<template>
  <main-menu />
  <div v-if="data">
    <navigation-button :storyId="sId" :chapterNo="cNo">
      <font-size @decrease="decreaseFont" @increase="increaseFont" />
    </navigation-button>

    <chapter-content
      :data="data"
      :fontSize="fontSize"
      :isLoading="isLoading"
      @hover-paragraph="setHovered"
      @clear-hover="clearHovered"
      @save-progress="saveReadingProgress"
    />
  </div>
  <q-inner-loading :showing="!data">
    Loading...
    <q-spinner-hearts size="50px" color="gold" />
  </q-inner-loading>

  <div class="row justify-center q-ma-md">
    <div class="col-10 row justify-end q-mb-lg">
      <q-btn
        unelevated
        color="purple"
        text-color="black"
        class="q-mr-md"
        round
        v-if="data && isEndReached"
        :icon="showCommentSection ? 'comment' : 'comments_disabled'"
        @click="showCommentSection = !showCommentSection"
      >
        <q-tooltip>
          {{ showCommentSection ? "Hide comments" : "Show comments" }}
        </q-tooltip>
      </q-btn>
      <to-top-button v-if="data" />
    </div>
  </div>
  <comment-section
    :data="data"
    :storyId="sId"
    :showCommentSection="showCommentSection"
    :trigger="trigger"
    :replyToId="replyToId"
    @refresh-comments="triggerCommentDownload"
    @reply="handleReply"
    @clear-reply="clearReply"
  />
</template>

<script setup lang="ts">
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from "vue";
import { fetchChapterWithParagraphs } from "../services/chapterservice";
import { useRoute } from "vue-router";
import { Chapter } from "../types/Chapter";
import { ReadingProgress } from "src/types/ReadingProgress";

import debounce from "lodash/debounce";
import ToTopButton from "src/utils/ToTopButton.vue";
import FontSize from "src/utils/FontSize.vue";
import NavigationButton from "src/utils/NavigationButton.vue";
import ChapterContent from "./ChapterContent.vue";
import CommentSection from "src/comment/CommentSection.vue";

const data = ref<Chapter>(null);

const sId = ref<number>(0);
const cNo = ref<number>(0);

// font increase and decrease
const fontSize = ref<number>(16);

const increaseFont = (): void => {
  fontSize.value += 2;
};

const decreaseFont = (): void => {
  if (fontSize.value > 8) fontSize.value -= 2;
};

// navigation between chapters
const route = useRoute();

// lazy loading chapters and scrolling

const offset = ref<number>(0);
const limit: number = 30;
const isLoading = ref<boolean>(false);
const isEndReached = ref<boolean>(false);

onMounted(() => {
  loadChapter();
  window.addEventListener("scroll", onScroll as EventListener);
});

onBeforeUnmount(() => {
  window.removeEventListener("scroll", onScroll as EventListener);
});

const onScroll = debounce((): void => {
  if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 200) {
    loadParagraphs();
  }
}, 300);

watch(
  () => route.query,
  () => {
    loadChapter();
  }
);

const loadParagraphs = async (): Promise<void> => {
  if (isEndReached.value) return;
  isLoading.value = true;
  try {
    const response = await fetchChapterWithParagraphs(
      sId.value,
      cNo.value,
      offset.value,
      limit
    );
    if (response.paragraphs.length < limit) {
      isEndReached.value = true;
    }
    data.value.paragraphs.push(...response.paragraphs);
    offset.value += limit;
  } catch (error) {
    console.error("Paragraphs loading error:", error);
  }
  isLoading.value = false;
};

const loadChapter = async () => {
  sId.value = Number(route.query.storyId);
  cNo.value = Number(route.query.chapterNo);
  data.value = null;
  offset.value = 0;
  isEndReached.value = false;

  isLoading.value = true;
  try {
    const response = await fetchChapterWithParagraphs(
      sId.value,
      cNo.value,
      offset.value,
      limit
    );
    data.value = { ...response, paragraphs: response.paragraphs };
    offset.value += response.paragraphs.length;

    await ensureLastReadParagraphLoaded();
  } catch (error) {
    console.error("Chapter loading error:", error);
  }
  isLoading.value = false;
};

const ensureLastReadParagraphLoaded = async () => {
  const progress = getReadingProgress();

  if (
    !progress ||
    progress.storyId !== data.value.storyDTO.id ||
    progress.chapterNo !== data.value.chapterNo
  )
    return;

  let attempts = 0;
  const maxAttempts = 100;
  let lastParagraphCount = 0;

  while (attempts < maxAttempts) {
    await nextTick();

    const element = document.getElementById(
      `paragraph-${progress.paragraphId}`
    );

    if (element) {
      element.scrollIntoView({ behavior: "smooth", block: "start" });
      return;
    }

    if (isEndReached.value) {
      break;
    }

    if (data.value.paragraphs.length === lastParagraphCount) {
      break;
    }

    lastParagraphCount = data.value.paragraphs.length;
    await loadParagraphs();
    await new Promise((resolve) => setTimeout(resolve, 40));

    attempts++;
  }
};

const saveReadingProgress = (paragraphId: number) => {
  if (data.value.storyDTO) {
    const progressList = JSON.parse(
      localStorage.getItem("readingProgress") || "[]"
    )
    const existingProgressIndex = progressList.findIndex(
      (progress: ReadingProgress) =>
        progress.storyId === data.value.storyDTO.id
    );

    if (existingProgressIndex !== -1) {
      progressList[existingProgressIndex] = {
        storyId: data.value.storyDTO.id,
        chapterNo: data.value.chapterNo,
        paragraphId,
      };
    } else {
      progressList.push({
        storyId: data.value.storyDTO.id,
        chapterNo: data.value.chapterNo,
        paragraphId,
      });
    }
    localStorage.setItem("readingProgress", JSON.stringify(progressList));
}};

const getReadingProgress = (): ReadingProgress | null => {
  const progressList = JSON.parse(localStorage.getItem("readingProgress") || "[]");
  return progressList.find(
    (progress: ReadingProgress) =>
      progress.storyId === data.value.storyDTO?.id
  ) || null;
};

// comment section
const showCommentSection = ref<boolean>(false);

const trigger = ref<boolean>(false);

const triggerCommentDownload = (): void => {
  trigger.value = !trigger.value;
};

const replyToId = ref<number | null>(null);

const handleReply = (id: number): void => {
  replyToId.value = id;
};

const clearReply = (): void => {
  replyToId.value = null;
};

// paragraph hover - for saving user reading progress and comments section (in the future)

const hoveredParagraph = ref<number>(null);
let hoverTimeout: NodeJS.Timeout = null;

const setHovered = (id: number): void => {
  clearTimeout(hoverTimeout);
  hoveredParagraph.value = id;
};

const clearHovered = (): void => {
  hoverTimeout = setTimeout(() => {
    hoveredParagraph.value = null;
  }, 300);
};
</script>
