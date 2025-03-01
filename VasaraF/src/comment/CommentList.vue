<template>
  <div v-for="comment in comments" key="comment.id">
    <comment-item
      :comment="comment"
      @comment-deleted="sendTrigger"
      @reply-to="handleReply"
      :storyAuthorId="storyAuthorId"
      :depth='0'
    />
  </div>
</template>

<script setup lang="ts">
import { getComments } from "src/services/commentservice";
import { onMounted, ref, watch } from "vue";
import { ChapterComment } from "src/types/ChapterComment";
import CommentItem from "./CommentItem.vue";

const comments = ref<ChapterComment[]>([]);

const emit = defineEmits(["comment-deleted", "reply"]);

const props = defineProps<{
  chapterId: number;
  trigger: boolean;
  storyAuthorId: number;
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
    comments.value = data;
  });
});

const sendTrigger = () => {
  emit("comment-deleted");
};

const handleReply = (id: number) => {
  emit('reply', id)
}

</script>
