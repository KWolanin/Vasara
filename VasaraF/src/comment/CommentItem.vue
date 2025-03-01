<template>
  <q-card class="card q-pa-md q-ml-md q-mr-md q-mb-md" flat>
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
  </q-card>

  <q-dialog v-model="deleteDialogVisible" persistent>
    <q-card class="q-ma-lg q-pa-sm" flat>
      <q-card-section class="row items-center">
        <span class="q-ml-sm"
          >Are you sure you want to delete this comment? It cannot be
          undone</span
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
import { ref } from "vue";
import { Notify } from "quasar";
import { deleteCommentFromDb } from "src/services/commentservice";

const deleteDialogVisible = ref<boolean>(false);
const commentToDelete = ref<number>(0);

const emit = defineEmits(["comment-deleted"]);

defineProps<{
  comment: ChapterComment;
  deletable: boolean;
}>();

const formatDate = (date: string | Date): string => {
  return format(new Date(date), "dd.MM.yyyy HH:mm");
};

const deleteComment = () => {
  if (commentToDelete.value > 0) {
    const id = commentToDelete.value;
    deleteCommentFromDb(id)
      .then(() => {
        Notify.create({
          message: "Comment has been deleted!",
          position: "bottom-right",
          type: "positive",
        });
        emit("comment-deleted");
      })
      .catch((err) => {
        console.error(err);
        Notify.create({
          message: "Something went wrong!",
          position: "bottom-right",
          type: "negative",
        });
      });
}}

const openDeleteDialog = (id: number): void => {
  deleteDialogVisible.value = true;
  commentToDelete.value = id;
};

</script>

<style scoped></style>
