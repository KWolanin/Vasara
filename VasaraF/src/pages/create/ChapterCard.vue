<template>
  <q-card class="q-pa-md" dense>
    <p class="text-h6">
      {{ chapter.chapterNo }}
      <q-input v-model="chapter.chapterTitle" outlined />
    </p>
    <q-space/>
    <!-- <q-btn class="q-mr-sm">Edit</q-btn> -->
     <div style="display: flex; justify-content: flex-end;">
    <q-btn flat class="del" @click="deleteChapter">Delete</q-btn>
     </div>
  </q-card>
</template>


<script setup lang="ts">

const emit = defineEmits()

const props = defineProps({
  chapter:  {
    type: Object,
    required: true
}})

const deleteChapter = () => {
  emit('delete-chapter', props.chapter.id)
}

</script>
