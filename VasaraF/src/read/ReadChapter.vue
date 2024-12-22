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
        <div class="row justify-center">
          <q-btn @click="decreaseFont" label="A-" class="q-mr-sm btn" flat />
          <q-btn @click="increaseFont" label="A+" class="btn" flat />
        </div>
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
      <h2>{{ data.chapterNo }}: {{ data.chapterTitle }}</h2>
      <q-card class="q-pa-md content" flat>
        <div :style="{ fontSize: fontSize + 'px' }" v-html="data.content" />
      </q-card>
    </div>
  </div>
  <q-inner-loading :showing="!data">
    Loading...
    <q-spinner-gears size="50px" color="primary" />
  </q-inner-loading>
</template>

<script setup>
import { onMounted, ref, watch } from "vue";
import { fetchChapter, isNextOrPrevious } from "../services/chapterservice";
import MainMenu from "../components/MainMenu.vue";
import { useRoute } from "vue-router";

const route = useRoute();

const data = ref(null);

const fontSize = ref(16);
const isNextChapter = ref(false);
const isPreviousChapter = ref(false);

const sId = ref(0);
const cNo = ref(0);

const increaseFont = () => {
  fontSize.value += 2;
};

const decreaseFont = () => {
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

const loadChapter = () => {
  sId.value = Number(route.query.storyId);
  cNo.value = Number(route.query.chapterNo);

  fetchChapter(route.query.storyId, route.query.chapterNo)
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
.btn {
  font-family: "Farro", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: #333 !important;
}

a:visited {
  color: #333;
}

.chapter {
  justify-items: center !important;
}

.content {
  width: 80%;
}
</style>
