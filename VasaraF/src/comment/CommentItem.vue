<template>
<q-card class="card q-pa-sm q-ma-sm" flat>
  <div class="row">
    <div class="text-weight-bold">{{ comment.name }}</div>
    <q-space/>
    <div>{{ formatDate(comment.createdAt) }}</div>
  </div>

  <div>{{ comment.content }}</div>

</q-card>
</template>


<script setup lang="ts">
import { format } from "date-fns";
import { ChapterComment } from 'src/types/ChapterComment'

const props = defineProps<{
  comment: ChapterComment;
}>();

const formatDate = (date: string | Date): string => {
  return format(new Date(date), "dd.MM.yyyy HH:mm");
};



</script>

<style scoped>



</style>
