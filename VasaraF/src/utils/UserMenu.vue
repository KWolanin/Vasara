<template>
<q-btn-dropdown
 :label="welcome" size="md" flat text-color="black" class="q-ml-md welcome">
      <q-list>

        <q-item clickable v-close-popup @click="toFollows">
          <q-item-section>
            <q-item-label>Follows, favourites & read later</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-close-popup @click="manage">
          <q-item-section>
            <q-item-label>Manage account</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-close-popup @click="profile">
          <q-item-section>
            <q-item-label>My profile</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-close-popup @click="logout">
          <q-item-section>
            <q-item-label>Logout</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-btn-dropdown>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useUserStore } from "src/stores/user";
import { useRouter } from "vue-router";

const router = useRouter();
const userStore = useUserStore();

const logout = () => {
  userStore.logout();
    if (router.currentRoute.value.path === "/readAll") {
    window.location.reload()
  } else {
    router.push("/readAll")
  }
};

const props = defineProps<{
  username: string;
}>();

const welcome = computed<string>(() => {
  return `Hello, ${props.username}!`;
})

const manage = (): void => {
  router.push("/account");
};

const toFollows = (): void => {
  router.push("/favs");
};

const totoRead = (): void => {
  router.push("/toread");
};

const profile = (): void => {
  router.push(`/user?authorId=${userStore.id}`);
};

</script>

