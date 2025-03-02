<template>
  <q-card
    class="card q-pa-md q-ml-md q-mr-md q-mb-md"
    :style="{
  marginLeft: depth * 30 + 'px',
  borderLeft: depth > 0 ? `solid ${depth + 1}px gold` : 'none'
}"
    flat
  >
    <div class="row">
      <div class="text-weight-bold">{{ comment.name }}</div>
      <q-space />
      <div class="text-italic">
        {{ formatDate(comment.createdAt) }}
        <q-btn
          v-if="deletable"
          class="q-ml-sm"
          color="burgund"
          unelevated
          flat
          icon="delete_forever"
          @click="openDeleteDialog(comment.id)"
        ></q-btn>
      </div>
    </div>

    <div>{{ comment.content }}</div>
    <q-card-actions>
      <q-space />
      <q-btn icon="reply" flat @click="reply">
        <q-tooltip>Reply</q-tooltip>
      </q-btn>
    </q-card-actions>
  </q-card>

    <comment-item
    v-for="reply in comment.replies"
    :key="reply.id"
      :comment="reply"
      :story-author-id="storyAuthorId"
      @reply-to="emit('reply-to', $event)"
      :depth="depth + 1"
      @comment-deleted="emit('comment-deleted', $event)"
    />

  <q-dialog v-model="deleteDialogVisible" persistent>
    <q-card class="q-ma-lg q-pa-sm" flat>
      <q-card-section class="row items-center">
        <span class="q-ml-sm"
          >Are you sure you want to delete this comment? It cannot be
          undone. If you delete parent comment, all replies will be deleted too.</span
        >
      </q-card-section>

      <q-card-actions align="right">
        <q-btn
          flat
          label="Cancel"
          color="gray"
          v-close-popup
          @click="deleteDialogVisible = false"
        />
        <q-btn
          flat
          label="Delete anyway"
          color="red"
          v-close-popup
          @click="deleteComment"
        />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { format } from "date-fns";
import { ChapterComment } from "src/types/ChapterComment";
import { ref, computed } from "vue";
import { deleteCommentFromDb } from "src/services/commentservice";
import { useUserStore } from "src/stores/user";
import { showNotification } from "src/utilsTS/notify";

const store = useUserStore();

const deleteDialogVisible = ref<boolean>(false);
const commentToDelete = ref<number>(0);

const emit = defineEmits(["comment-deleted", "reply-to"]);

const props = defineProps<{
  comment: ChapterComment;
  storyAuthorId: number;
  depth: number;
}>();

const deletable = computed(() => {
  return props.storyAuthorId == store.id;
});

const formatDate = (date: string | Date): string => {
  return format(new Date(date), "dd.MM.yyyy HH:mm");
};

const deleteComment = () => {
  if (commentToDelete.value > 0) {
    const id = commentToDelete.value;
    deleteCommentFromDb(id)
      .then(() => {
        showNotification("Comment deleted!", "positive");
        emit("comment-deleted");
      })
      .catch((err) => {
        console.error(err);
        showNotification("Something went wrong!", "negative")
      });
  }
};

const openDeleteDialog = (id: number): void => {
  deleteDialogVisible.value = true;
  commentToDelete.value = id;
};

const reply = (): void => {
  emit("reply-to", props.comment.id);
};
</script>

