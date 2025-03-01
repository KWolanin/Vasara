<template>
  <div v-for="comment in comments" key="comment.id">
    <comment-item :comment="comment" :deletable="deletable" @comment-deleted="sendTrigger"/>
  </div>
</template>

<script setup lang="ts">
import { getComments } from "src/services/commentservice";
import { onMounted, ref, watch } from "vue";
import { ChapterComment } from "src/types/ChapterComment";
import CommentItem from "./CommentItem.vue";

const comments = ref<ChapterComment[]>([]);

const emit = defineEmits(["comment-deleted"]);


const props = defineProps<{
  chapterId: number;
  trigger: boolean;
  deletable: boolean;
}>();

watch(
  () => props.trigger,
  () => {
    getComments(props.chapterId).then((data) => {
      comments.value = data;
    });
  }
);

onMounted(() => {
  getComments(props.chapterId).then((data) => {
    console.log(data);
    comments.value = data;
  });
});

const sendTrigger = () => {
  emit("comment-deleted");
}

</script>
