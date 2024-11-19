<template>
  <div class="row justify-center">
    <q-card v-if="data" class="col-10 q-pa-md">
      <div v-html="data.content" />
    </q-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { fetchChapter } from "../services/chapterservice";

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

onMounted(() => {
  fetchChapter(props.storyId, props.chapterNo)
    .then((response) => {
      console.log(response);
      data.value = response;
      console.log(data.value);
    })
    .catch((error) => {
      console.error(error);
    });
});
</script>
