<template>
  <q-card class="q-pa-md" dense>
    <p class="text-h6">
      <q-input v-model="chapter.chapterTitle" outlined />
      <!-- {{chapter.chapterTitle}} -->
    </p>
    <q-space/>
    <!-- <q-btn class="q-mr-sm">Edit</q-btn>
    <q-btn>Delete</q-btn> -->
  </q-card>
</template>


<script setup lang="ts">

defineProps({
  chapter:  {
    type: Object,
    required: true
}})


</script>
