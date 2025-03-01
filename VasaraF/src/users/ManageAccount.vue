<template>
  <main-menu />
  <div div class="row justify-center q-pa-lg">
    <q-card class="col-8 q-pa-lg card" flat>
      <span class="q-ml-md q-mb-lg">Manage your account</span>
      <div v-if="msg" class="msg">{{ msg }}</div>
      <div v-if="msgSuccess" class="msg-success">{{ msgSuccess }}</div>
      <div class="row q-ml-md col-4">
        <q-input v-model="userData.email" :disable="!editing.email"></q-input>
        <q-btn class="q-ml-md" flat @click="updateUserData('email')">
          {{ editing.email ? "Save 🔧" : "Change e-mail 🔧" }}
          <q-tooltip>
            Change the email associated with your account, where you will
            receive notifications.
          </q-tooltip>
        </q-btn>
      </div>
      <div class="row q-ml-md col-4">
        <q-input v-model="userData.username" :disable="!editing.username"></q-input>
        <q-btn class="q-ml-md" flat @click="updateUserData('username')">
          {{ editing.username ? "Save 🔧" : "Change username 🔧" }}
          <q-tooltip>
            Change the displayed username (the login will remain the same as the
            one provided during registration).
          </q-tooltip>
        </q-btn>
      </div>
      <div class="row q-ml-md col-4">
        <q-input
          v-model="userData.password"
          :disable="!editing.password"
          type="password"
          placeholder="*******"
        ></q-input>
        <q-btn class="q-ml-md" flat @click="updateUserData('password')">
          {{ editing.password ? "Save 🔧" : "Set new password 🔧" }}
        </q-btn>
      </div>
      <div class="row q-ml-md q-mt-md col-4">
        <q-input
          v-model="userData.description"
          :disable="!editing.description"
          filled
          type="textarea"
          placeholder="My name is..."
        ></q-input>
        <q-btn class="q-ml-md" flat @click="updateUserData('description')">
          {{ editing.description ? "Save 🔧" : "Set new description 🔧" }}
        </q-btn>
      </div>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import MainMenu from "../utils/MainMenu.vue";
import { useUserStore } from "src/stores/user";
import { ref, onMounted } from "vue";
import { change } from "src/services/userservice";
import { getAuthorDescriptionById } from "src/services/authorservice";
import { UpdateAuthorRequest } from "src/types/UpdateAuthorRequest";

const userStore = useUserStore();

const userData = ref({
  email: "",
  oldEmail: "",
  username: "",
  oldUsername: "",
  password: "",
  description: "",
  oldDescription: "",
});

const editing = ref({
  email: false,
  username: false,
  password: false,
  description: false,
});

const msg = ref("");
const msgSuccess = ref("");

onMounted(() => {
  userData.value.email = userStore.email;
  userData.value.oldEmail = userStore.email;
  userData.value.username = userStore.username;
  userData.value.oldUsername = userStore.username;

  getAuthorDescriptionById(userStore.id).then((desc) => {
    userData.value.description = desc;
    userData.value.oldDescription = desc;
  });
});

const updateUserData = async (field: keyof UpdateAuthorRequest): Promise<void> => {
  msg.value = "";
  msgSuccess.value = "";
  if (editing.value[field]) {
    const updatedValue = userData.value[field as keyof typeof userData.value];
    const oldValue = userData.value[`old${capitalize(field)}` as keyof typeof userData.value];

    if (field !== "password" && updatedValue === oldValue) {
      editing.value[field] = false;
      return;
    }

    const updatePayload: UpdateAuthorRequest = {
      id: userStore.id,
      email: null,
      username: null,
      login: null,
      password: null,
      description: null,
      [field]: updatedValue,
    };

    try {
      await change(updatePayload);
      msgSuccess.value = `${capitalize(field)} updated successfully`.toUpperCase();

      if (field === "email") userStore.updateEmail(updatedValue as string);
      if (field === "username") userStore.updateUsername(updatedValue as string);

      userData.value[`old${capitalize(field)}` as keyof typeof userData.value] = updatedValue;
    } catch(error) {
      msg.value = `Failed to update ${field}: ${error.response.data.message}`.toUpperCase();
    }
  }
  editing.value[field] = !editing.value[field];
};

const capitalize = (str: string) => str.charAt(0).toUpperCase() + str.slice(1);
</script>

<style scoped>
.msg {
  color: red;
  margin-top: 10px;
  text-align: center;
  font-size: small;
  font-weight: 700;
}

.msg-success {
  color: rgb(69, 114, 0);
  margin-top: 10px;
  text-align: center;
  font-size: small;
  font-weight: 700;
}
</style>
