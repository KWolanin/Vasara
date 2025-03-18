<template>
  <div class="row justify-center q-ma-md">
    <div class="col-10 row justify-between items-center">
      <router-link
        v-show="isPreviousChapter"
        :to="{
          name: 'readChapter',
          query: { storyId: storyId, chapterNo: chapterNo - 1 },
        }"
      >
        <q-btn class="q-ml-md btn" label="Previous" icon="arrow_back" flat />
      </router-link>
      <q-space />
      <slot />
      <q-space />
      <router-link
        v-show="isNextChapter"
        :to="{
          name: 'readChapter',
          query: { storyId: storyId, chapterNo: chapterNo + 1 },
        }"
      >
        <q-btn class="q-mr-md btn" icon="arrow_forward" label="Next" flat />
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { isNextOrPrevious } from 'src/services/chapterservice';
import { ref, onMounted } from 'vue'

const props = defineProps({
  storyId: Number,
  chapterNo: Number,
});


const isNextChapter = ref<boolean>(false);
const isPreviousChapter = ref<boolean>(false);

onMounted(() => {
  isNextOrPrevious(props.storyId, props.chapterNo + 1).then((response) => {
    isNextChapter.value = response;
  });
  isNextOrPrevious(props.storyId, props.chapterNo - 1).then((response) => {
    isPreviousChapter.value = response;
  });
})
</script>
