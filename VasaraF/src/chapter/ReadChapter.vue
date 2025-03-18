<template>
  <main-menu />
  <div v-if="data">
    <div class="row justify-center q-ma-md">
      <div class="col-10 row justify-between items-center">
        <router-link
          v-show="isPreviousChapter"
          :to="{
            name: 'readChapter',
            query: { storyId: sId, chapterNo: cNo - 1 },
          }"
        >
          <q-btn class="q-ml-md btn" label="Previous" icon="arrow_back" flat />
        </router-link>
        <q-space />
        <div class="row justify-center">
          <q-btn @click="decreaseFont" label="A-" class="q-mr-sm btn" flat />
          <q-btn @click="increaseFont" label="A+" class="btn" flat />
        </div>
        <q-space />
        <router-link
          v-show="isNextChapter"
          :to="{
            name: 'readChapter',
            query: { storyId: sId, chapterNo: cNo + 1 },
          }"
        >
          <q-btn class="q-mr-md btn" icon="arrow_forward" label="Next" flat />
        </router-link>
      </div>
    </div>

    <div class="q-mb-md chapter">
      <h4>{{ data.chapterTitle }}</h4>
      <div class="content-wrapper">
        <q-card class="q-pa-md q-mb-lg content" flat>
          <div
            class="paragraph-container"
            v-for="paragraph in data.paragraphs"
            :key="paragraph.id"
            :style="{ fontSize: fontSize + 'px' }"
            @mouseover="setHovered(paragraph.id)"
            @mouseleave="clearHovered"
            @mouseenter="saveReadingProgress(paragraph.id)"
            :id="'paragraph-' + paragraph.id"
          >
            <!-- <q-btn // DON'T REMOVE: needed to rework the comment system in the future
              v-if="hoveredParagraph === paragraph.id"
              icon="add_comment"
              flat
              dense
              size="sm"
              class="comment-btn"
              @mouseover="setHovered(paragraph.id)"
              @mouseleave="clearHovered"
            /> -->
            <p class="paragraph" v-html="sanitized(paragraph.content)"></p>
          </div>
          <q-spinner-hearts v-if="isLoading" size="50px" color="gold" />
        </q-card>
      </div>
    </div>
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
      <q-btn
        unelevated
        color="pink"
        v-if="data && isEndReached"
        round
        text-color="black"
        icon="arrow_circle_up"
        class="q-mr-md"
        @click="toTop"
      >
        <q-tooltip> To top </q-tooltip>
      </q-btn>
    </div>
  </div>
  <div class="row justify-center q-ma-md">
    <div class="col-5 column justify-center q-mb-lg" v-if="showCommentSection">
      <comment-list
        :chapterId="data.id"
        :trigger="trigger"
        @comment-deleted="triggerCommentDownload"
        :storyAuthorId="data.storyDTO.authorId"
        @reply="handleReply"
      />
      <comment-editor
        :storyId="sId"
        :chapterId="data.id"
        :parentId="replyToId"
        @comment-added="triggerCommentDownload"
        @comment-sent="clearReply"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  nextTick,
  onBeforeUnmount,
  onMounted,
  ref,
  watch,
} from "vue";
import {
  fetchChapterWithParagraphs,
  isNextOrPrevious,
} from "../services/chapterservice";
import { useRoute } from "vue-router";
import { Chapter } from "../types/Chapter";
import { ReadingProgress } from "src/types/ReadingProgress";
import CommentEditor from "src/comment/CommentEditor.vue";
import CommentList from "src/comment/CommentList.vue";
import DOMPurify from "dompurify";
import { debounce } from "lodash";


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

const isNextChapter = ref<boolean>(false);
const isPreviousChapter = ref<boolean>(false);




// lazy loading chapters and scrolling

const offset = ref<number>(0);
const limit : number = 30;
const isLoading = ref<boolean>(false);
const isEndReached = ref<boolean>(false);

onMounted(() => {
  loadChapter();
  window.addEventListener("scroll", onScroll);
});

onBeforeUnmount(() => {
  window.removeEventListener("scroll", onScroll);
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

const sanitized = (html: string): string => DOMPurify.sanitize(html);

const loadParagraphs = async () : Promise<void> => {
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

  isNextOrPrevious(sId.value, cNo.value + 1).then((response) => {
    isNextChapter.value = response;
  });
  isNextOrPrevious(sId.value, cNo.value - 1).then((response) => {
    isPreviousChapter.value = response;
  });
};

const ensureLastReadParagraphLoaded = async () => {
  const progress = getReadingProgress();

  if (!progress || progress.storyId !== data.value.storyDTO.id
  || progress.chapterNo !== data.value.chapterNo) return;

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

const toTop = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

const saveReadingProgress = (paragraphId: number) => {
  if (data.value.storyDTO) {
    localStorage.setItem(
      "readingProgress",
      JSON.stringify({
        storyId: data.value.storyDTO.id,
        chapterNo: data.value.chapterNo,
        paragraphId,
      })
    );
  }
};

const getReadingProgress = (): ReadingProgress => {
  const progress = localStorage.getItem("readingProgress");
  return progress ? JSON.parse(progress) : null;
};

// comment section
const showCommentSection = ref<boolean>(false);

const trigger = ref<boolean>(false);

const triggerCommentDownload = () :void => {
  trigger.value = !trigger.value;
};

const replyToId = ref<number | null>(null);

const handleReply = (id: number) :void => {
  replyToId.value = id;
};

const clearReply = () :void => {
  replyToId.value = null;
};

// paragraph hover - for saving user reading progress and comments section (in the future)

const hoveredParagraph = ref<number>(null);
let hoverTimeout: NodeJS.Timeout  = null;

const setHovered = (id: number) :void => {
  clearTimeout(hoverTimeout);
  hoveredParagraph.value = id;
};

const clearHovered = () :void => {
  hoverTimeout = setTimeout(() => {
    hoveredParagraph.value = null;
  }, 300);
};


</script>

<style scoped>
.chapter {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 0;
  margin: 0 auto;
  flex-wrap: wrap;
}

.content {
  width: 80%;
  will-change: transform;
}

.content-wrapper {
  width: 100%;
  display: flex;
  justify-content: center;
}

.paragraph-container {
  display: flex;
  align-items: center;
  position: relative;
}

.paragraph {
  margin: 0 0 1px;
}

.comment-btn {
  position: absolute;
  right: -30px;
  transition: opacity 0.2s ease-in-out;
}
</style>
