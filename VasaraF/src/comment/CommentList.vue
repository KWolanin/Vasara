<template>
  <div v-for="comment in comments">
    <comment-item :comment="comment" />
    </div>
</template>

<script setup lang="ts">
import { getComments } from 'src/services/commentservice';
import { onMounted, ref, watch } from 'vue';
import { ChapterComment } from 'src/types/ChapterComment';
import CommentItem from './CommentItem.vue';


const comments = ref<ChapterComment[]>([])

const props = defineProps<{
  chapterId: number;
  trigger: boolean;
}>();

watch(
  () => props.trigger,
  () => {
    getComments(props.chapterId).then(data => {
    comments.value = data
  })
  }
);

onMounted(() => {
  getComments(props.chapterId).then(data => {
    comments.value = data
  })
})

</script>
