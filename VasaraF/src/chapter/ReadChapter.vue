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
      <q-card class="q-pa-md q-mb-lg content" flat>
        <div
          class="q-ma-md"
          :style="{ fontSize: fontSize + 'px' }"
          v-html="data.content"
        />
      </q-card>
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
        v-if="data"
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
        v-if="data"
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
import { onMounted, ref, watch, computed } from "vue";
import { fetchChapter, isNextOrPrevious } from "../services/chapterservice";
import MainMenu from "../utils/MainMenu.vue";
import { useRoute } from "vue-router";
import { Chapter } from "../types/Chapter";
import CommentEditor from "src/comment/CommentEditor.vue";
import CommentList from "src/comment/CommentList.vue";
import { useUserStore } from "src/stores/user";

const store = useUserStore();

const route = useRoute();

const data = ref<Chapter>(null);

const fontSize = ref<number>(16);
const isNextChapter = ref<boolean>(false);
const isPreviousChapter = ref<boolean>(false);

const sId = ref<number>(0);
const cNo = ref<number>(0);

const increaseFont = (): void => {
  fontSize.value += 2;
};

const decreaseFont = (): void => {
  if (fontSize.value > 8) fontSize.value -= 2;
};

onMounted(() => {
  loadChapter();
});

watch(
  () => route.query,
  () => {
    loadChapter();
  }
);

const loadChapter = (): void => {
  sId.value = Number(route.query.storyId);
  cNo.value = Number(route.query.chapterNo);

  fetchChapter(Number(route.query.storyId), Number(route.query.chapterNo))
    .then((response) => {
      data.value = response;
    })
    .catch((error) => {
      console.error(error);
    });

  isNextOrPrevious(sId.value, cNo.value + 1).then((response) => {
    isNextChapter.value = response;
  });
  isNextOrPrevious(sId.value, cNo.value - 1).then((response) => {
    isPreviousChapter.value = response;
  });
};

const toTop = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

const showCommentSection = ref<boolean>(false);

const trigger = ref<boolean>(false);

const triggerCommentDownload = () => {
  trigger.value = !trigger.value;
};


const replyToId = ref<number | null>(null);

const handleReply = (id: number) => {
  replyToId.value = id;
};

const clearReply = () => {
  replyToId.value = null;
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
}
.content {
  width: 80%;
}
</style>
