<template>
  <div class="row justify-center q-ma-md">
    <div class="col-5 column justify-center q-mb-lg" v-if="showCommentSection">
      <comment-list
        :chapterId="data.id"
        :trigger="trigger"
        @comment-deleted="$emit('refresh-comments')"
        :storyAuthorId="data.storyDTO.authorId"
        @reply="$emit('reply', $event)"
      />
      <comment-editor
        :storyId="storyId"
        :chapterId="data.id"
        :parentId="replyToId"
        @comment-added="$emit('refresh-comments')"
        @comment-sent="$emit('clear-reply')"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import CommentEditor from "src/comment/CommentEditor.vue";
import CommentList from "src/comment/CommentList.vue";


defineProps({
  data: Object,
  storyId: Number,
  showCommentSection: Boolean,
  trigger: Boolean,
  replyToId: Number,
});
</script>

