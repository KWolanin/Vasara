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
          <q-btn class="q-ml-md btn" label="Previous" flat />
        </router-link>
        <q-space/>
        <div class="row justify-center">
          <q-btn @click="decreaseFont" label="A-" class="q-mr-sm btn" flat />
          <q-btn @click="increaseFont" label="A+" class="btn" flat />
        </div>
        <q-space/>
        <router-link
          v-show="isNextChapter"
          :to="{
            name: 'readChapter',
            query: { storyId: sId, chapterNo: cNo + 1 },
          }"
        >
          <q-btn class="q-mr-md btn" label="Next" flat />
        </router-link>
      </div>
    </div>

    <div class="q-mb-md chapter">
      <h4>{{ data.chapterTitle }}</h4>
      <q-card class="q-pa-md content" flat>
        <div :style="{ fontSize: fontSize + 'px' }" v-html="data.content" />
      </q-card>
    </div>
  </div>
  <q-inner-loading :showing="!data">
    Loading...
    <q-spinner-hearts size="50px" color="amber" />
  </q-inner-loading>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import { fetchChapter, isNextOrPrevious } from "../services/chapterservice";
import MainMenu from "../utils/MainMenu.vue";
import { useRoute } from "vue-router";
import { Chapter } from "../types/Chapter";

const route = useRoute();

const data = ref<Chapter>(null);

const fontSize = ref<number>(16);
const isNextChapter = ref<boolean>(false);
const isPreviousChapter = ref<boolean>(false);

const sId = ref<number>(0);
const cNo = ref<number>(0);

const increaseFont = () : void => {
  fontSize.value += 2;
};

const decreaseFont = () : void => {
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

const loadChapter = () : void => {
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
</script>

<style scoped>

.chapter {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  width: 100%;
  padding: 0;
  margin: 0 auto;
}
.content {
  width: 80%;
}
</style>
