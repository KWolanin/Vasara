<template>
<main-menu/>
<div div class="row justify-center q-pa-lg">
  <q-card class="col-8 q-pa-lg card" flat>
  <span class="q-ml-md q-mb-lg">Manage your account</span>
  <div v-if="msg" class="msg">{{ msg }}</div>
  <div class="row q-ml-md col-4">
    <q-input v-model="email" :disable="!editingEmail"></q-input>
    <q-btn class="q-ml-md" flat @click="changeUserEmail">
      {{ editingEmail ? 'Save 🔧' : 'Change e-mail 🔧' }}
      <q-tooltip>
        Change the email associated with your account, where you will receive notifications.
</q-tooltip>
    </q-btn>
  </div>
  <div class="row q-ml-md col-4">
      <q-input v-model="username" :disable="!editingUsername"></q-input>
    <q-btn class="q-ml-md" flat @click="changeUserName">
      {{ editingUsername ? 'Save 🔧' : 'Change username 🔧' }}
      <q-tooltip>
        Change the displayed username (the login will remain the same as the one provided during registration).      </q-tooltip>
    </q-btn>
  </div>
</q-card>
</div>

</template>

<script setup lang="ts">
import MainMenu from "../utils/MainMenu.vue";
import { useUserStore } from "src/stores/user";
import { ref, onMounted } from "vue";
import { changeEmail, changeUsername } from "src/services/userservice";

const userStore = useUserStore();

const email = ref<string>("")
const oldEmail = ref<string>("")

onMounted(() => {
  email.value = userStore.email
  oldEmail.value = userStore.email
  username.value = userStore.username
  oldusername.value = userStore.username
})

const editingEmail = ref<boolean>(false)
const msg = ref<string>("")

const changeUserEmail = async () : Promise<void> => {
  msg.value = ""
  if (editingEmail.value) {
    if (oldEmail.value !== email.value) {
      changeEmail(email.value, userStore.id).then((data) => {
        console.log(data)
        userStore.updateEmail(email.value)
        oldEmail.value = email.value
        msg.value = ""
      })
      .catch((error) => {
        console.error(error)
        msg.value = "Failed to update email"
    })
  }
}
editingEmail.value = !editingEmail.value
}

const username = ref<string>("")
const oldusername = ref<string>("")
const editingUsername = ref<boolean>(false)

const changeUserName = async () : Promise<void> => {
  msg.value = ""
  if (editingUsername.value) {
    if (oldusername.value !== username.value) {
      changeUsername(username.value, userStore.id).then((data) => {
        userStore.updateUsername(username.value)
        oldusername.value = username.value
        msg.value = ""
      })
      .catch(() => {
        msg.value = "Failed to update username"
    })
  }
}
editingUsername.value = !editingUsername.value
}

</script>

<style scoped>

.msg {
  color: red;
  margin-top: 10px;
  text-align: center;
  font-size: small;
  font-weight: 700;
}
</style>
