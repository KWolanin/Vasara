<template>
  <main-menu />
  <div div class="row justify-center q-pa-lg">
    <q-card class="col-8 q-pa-lg card" flat>
      <span class="q-ml-md q-mb-lg">Manage your account</span>
      <div v-if="msg" class="msg">{{ msg }}</div>
      <div v-if="msgSuccess" class="msg-success">{{ msgSuccess }}</div>
      <div class="row q-ml-md col-4">
        <q-input v-model="email" :disable="!editingEmail"></q-input>
        <q-btn class="q-ml-md" flat @click="changeUserEmail">
          {{ editingEmail ? "Save 🔧" : "Change e-mail 🔧" }}
          <q-tooltip>
            Change the email associated with your account, where you will
            receive notifications.
          </q-tooltip>
        </q-btn>
      </div>
      <div class="row q-ml-md col-4">
        <q-input v-model="username" :disable="!editingUsername"></q-input>
        <q-btn class="q-ml-md" flat @click="changeUserName">
          {{ editingUsername ? "Save 🔧" : "Change username 🔧" }}
          <q-tooltip>
            Change the displayed username (the login will remain the same as the
            one provided during registration).
          </q-tooltip>
        </q-btn>
      </div>
      <div class="row q-ml-md col-4">
        <q-input
          v-model="password"
          :disable="!editingPassword"
          type="password"
          placeholder="*******"
        ></q-input>
        <q-btn class="q-ml-md" flat @click="changeUserPassword">
          {{ editingPassword ? "Save 🔧" : "Set new password 🔧" }}
        </q-btn>
      </div>
      <div class="row q-ml-md q-mt-md col-4">
        <q-input
          v-model="description"
          :disable="!editingDescription"
          filled
          type="textarea"
          placeholder="My name is..."
        ></q-input>
        <q-btn class="q-ml-md" flat @click="changeDescription">
          {{ editingDescription ? "Save 🔧" : "Set new description 🔧" }}
        </q-btn>
      </div>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import MainMenu from "../utils/MainMenu.vue";
import { useUserStore } from "src/stores/user";
import { ref, onMounted } from "vue";
import {
  changeEmail,
  changeUsername,
  changePassword,
  changeDesc,
} from "src/services/userservice";
import {getAuthorDescriptionById} from "src/services/authorservice"

const userStore = useUserStore();

const email = ref<string>("");
const oldEmail = ref<string>("");

onMounted(() => {
  email.value = userStore.email;
  oldEmail.value = userStore.email;
  username.value = userStore.username;
  oldusername.value = userStore.username;

  getAuthorDescriptionById(userStore.id).then((desc) => {
    description.value = desc
  })
});

const editingEmail = ref<boolean>(false);
const msg = ref<string>("");
const msgSuccess = ref<string>("");

const changeUserEmail = async (): Promise<void> => {
  msg.value = "";
  if (editingEmail.value) {
    if (oldEmail.value !== email.value) {
      changeEmail(email.value, userStore.id)
        .then((data) => {
          userStore.updateEmail(email.value);
          oldEmail.value = email.value;
          msg.value = "";
          msgSuccess.value = "Email updated successfully";
        })
        .catch((error) => {
          console.error(error);
          msg.value = "Failed to update email";
        });
    }
  }
  editingEmail.value = !editingEmail.value;
};

const username = ref<string>("");
const oldusername = ref<string>("");
const editingUsername = ref<boolean>(false);

const changeUserName = async (): Promise<void> => {
  msg.value = "";
  if (editingUsername.value) {
    if (oldusername.value !== username.value) {
      changeUsername(username.value, userStore.id)
        .then((data) => {
          userStore.updateUsername(username.value);
          oldusername.value = username.value;
          msg.value = "";
          msgSuccess.value = "Username updated successfully";
        })
        .catch(() => {
          msg.value = "Failed to update username";
        });
    }
  }
  editingUsername.value = !editingUsername.value;
};

const password = ref<string>("");
const editingPassword = ref<boolean>(false);

const changeUserPassword = async (): Promise<void> => {
  msg.value = "";
  if (editingPassword.value) {
    changePassword(password.value, userStore.id)
      .then((data) => {
        msg.value = "";
        msgSuccess.value = "Password updated successfully";
      })
      .catch(() => {
        msg.value = "Failed to update password";
      });
  }
  editingPassword.value = !editingPassword.value;
};

const description = ref<string>("");
const editingDescription = ref<boolean>(false);

const changeDescription = async (): Promise<void> => {
  msg.value = "";
  if (editingDescription.value) {
    changeDesc(userStore.id, description.value)
      .then((data) => {
        msg.value = "";
        msgSuccess.value = "Description updated successfully";
      })
      .catch(() => {
        msg.value = "Failed to update description";
      });
  }
  editingDescription.value = !editingDescription.value;
};

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
