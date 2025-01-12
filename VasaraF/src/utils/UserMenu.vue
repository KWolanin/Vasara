<template>
<q-btn-dropdown
 :label="welcome" size="md" flat text-color="black" class="q-ml-md welcome">
      <q-list>
        <!-- temporary disabled - waiting for manage account implementation -->
<!--         <q-item clickable v-close-popup @click="manage">
          <q-item-section>
            <q-item-label>Manage account</q-item-label>
          </q-item-section>
        </q-item> -->

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
  router.push("/readAll");
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

</script>

