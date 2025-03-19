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
            placeholder="Write your throughs here"
            type="textarea"
            counter
            color="burgund"
            maxlength="500"
          />
        </div>

        <q-input
          v-model="username"
          :disable="isLoggedIn"
          placeholder="Username"
          color="burgund"
          :rules="[(val) => !!val || 'Field is required']"
        />
        <q-input
          v-model="email"
          :disable="isLoggedIn"
          placeholder="E-mail"
          color="burgund"
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
import { ref, computed, watch, onMounted, useTemplateRef } from "vue";
import { useUserStore } from "src/stores/user";
import { createComment, checkPermissions } from "src/services/commentservice";
import { showNotification } from "src/utilsTS/notify";
import { defineEmits } from "vue";

const emit = defineEmits(["comment-added", "comment-sent"]);

const store = useUserStore();

const isLoggedIn = computed(() => !!store.id);

// all refs should be string - because of quasar components
//  behavior returning string or number no matter what type a v-model is,
//  I made it string | number to avoid multiple typescript missmatch errors
const NewComment = ref<string | number>("");
const username = ref<string | number>(isLoggedIn.value ? store.username : "");
const email = ref<string | number>(isLoggedIn.value ? store.email : "");

const commentAllowed = ref<boolean>(false);
const guestCommentAllowed = ref<boolean>(false);

const props = defineProps<{
  storyId: number;
  chapterId: number;
  parentId: number | null;
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

const send = () : void => {
  if (!NewComment.value) {
    showNotification("Comment is empty!", "negative");
    return;
  }
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
      showNotification("Comment published!", "positive");
      clearForm();
      emit("comment-added");
      emit('comment-sent');
    })
    .catch(() => {
      showNotification("Something goes wrong!", "negative")
    });
};

const clearForm = ():void => {
  NewComment.value = "";
  isLoggedIn ? null : (email.value = "");
  isLoggedIn ? null : (username.value = "");
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
