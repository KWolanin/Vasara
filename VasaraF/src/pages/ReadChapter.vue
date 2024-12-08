<template>
  <main-menu />
  <div v-if="data">
    <div class="row justify-center q-ma-md">
      <div class="col-10 row justify-between items-center">
        <q-btn class="q-ml-md" label="Previous" />
        <div class="row justify-center">
          <q-btn @click="decreaseFont" label="A-" class="q-mr-sm" />
          <q-btn @click="increaseFont" label="A+" />
        </div>
        <q-btn class="q-mr-md" label="Next" />
      </div>
    </div>

    <div class="row justify-center q-mb-md">
      <q-card class="col-10 q-pa-md">
        <div :style="{ fontSize: fontSize + 'px' }" v-html="data.content" />
      </q-card>
    </div>
  </div>
  <q-inner-loading :showing="!data">
    Wczytywanie...
    <q-spinner-gears size="50px" color="primary" />
  </q-inner-loading>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { fetchChapter } from "../services/chapterservice";
import MainMenu from "./MainMenu.vue";

const props = defineProps({
  chapterNo: {
    type: Number,
    required: true,
  },
  storyId: {
    type: Number,
    required: true,
  },
});

const data = ref(null);
const title = ref("");
const number = ref(0);

const fontSize = ref(16);

const increaseFont = () => {
  fontSize.value += 2;
};

const decreaseFont = () => {
  if (fontSize.value > 8) fontSize.value -= 2;
};

onMounted(() => {
  fetchChapter(props.storyId, props.chapterNo)
    .then((response) => {
      data.value = response;
    })
    .catch((error) => {
      console.error(error);
    });
});
</script>
