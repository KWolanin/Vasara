<template>
  <div class="q-pa-md" v-if="allowComment">
    <q-card class="card q-pa-md" flat>
      <div class="header">What do you think?</div>
      <div class="header" v-if="props.parentId > 0">Send reply to the comment</div>
      <q-form @submit="send">
        <div class="flex justify-center">
          <q-input
            class="input"
            v-model="NewComment"
            filled
            type="textarea"
            counter
            maxlength="500"
            :rules="[(val) => !!val || 'Field is required']"
          />
        </div>

        <q-input
          v-model="username"
          :disable="isLoggedIn"
          placeholder="Username"
          :rules="[(val) => !!val || 'Field is required']"
        />
        <q-input
          v-model="email"
          :disable="isLoggedIn"
          placeholder="E-mail"
          type="email"
          :rules="[(val) => !!val || 'Field is required']"
        />
        <q-btn
          class="btn q-mt-md full-width"
          color="gold"
          text-color="black"
          unelevated
          type="submit"
          >SEND</q-btn
        >
      </q-form>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from "vue";
import { useUserStore } from "src/stores/user";
import { createComment, checkPermissions } from "src/services/commentservice";
import { Notify } from "quasar";

import { defineEmits } from "vue";

const emit = defineEmits(["comment-added", "comment-sent"]);

const store = useUserStore();

const isLoggedIn = computed(() => !!store.id);

const NewComment = ref<string>("");
const username = ref<string>(isLoggedIn.value ? store.username : "");
const email = ref<string>(isLoggedIn.value ? store.email : "");

const commentAllowed = ref<boolean>(false);
const guestCommentAllowed = ref<boolean>(false);

const props = defineProps<{
  storyId: number;
  chapterId: number;
  parentId: number;
}>();

watch(
  () => props.chapterId,
  () => {
    clearForm();
  }
);
const allowComment = computed(() =>
  isLoggedIn.value ? commentAllowed.value : guestCommentAllowed.value
);

onMounted(() => {
  checkPermissions(props.chapterId)
    .then((data) => {
      commentAllowed.value = data.commentAllowed;
      guestCommentAllowed.value = data.guestCommentAllowed;
    })
    .catch(() => {
      commentAllowed.value = false;
      guestCommentAllowed.value = false;
    });
});

const send = () => {
  const comment = {
    content: NewComment.value,
    name: username.value,
    parentId: props.parentId,
    storyId: props.storyId,
    chapterId: props.chapterId,
    email: email.value,
    createdAt: new Date(),
    replies: []
  };
  createComment(comment)
    .then(() => {
      Notify.create({
        message: "Comment published!",
        position: "bottom-right",
        type: "positive",
      });
      clearForm();
      emit("comment-added");
      emit('comment-sent');
    })
    .catch(() => {
      Notify.create({
        message: "Something goes wrong!",
        position: "bottom-right",
        type: "negative",
      });
    });
};

const clearForm = () => {
  NewComment.value = "";
  isLoggedIn ? null : (email.value = "");
  isLoggedIn ? null : (username.value = "");
 // parent.value = 0;
};
</script>

<style scoped>
.input {
  width: 500px;
}

.header {
  text-align: center;
  font-family: "Farro", sans-serif;
}
</style>
