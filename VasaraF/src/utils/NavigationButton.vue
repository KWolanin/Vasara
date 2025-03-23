<template>
  <div class="row justify-center q-ma-md">
    <div class="col-10 row justify-between items-center">
      <router-link
        v-show="previous"
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
        v-show="next"
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
defineProps({
  storyId: Number,
  chapterNo: Number,
  next: Boolean,
  previous: Boolean
});

</script>
