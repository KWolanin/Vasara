<template>
  <q-card v-if="visible" class="header q-pa-md q-mr-md q-mb-md card">
    <q-card-section class="row items-center q-pb-none">
      <q-space />
      <q-btn icon="close" flat round dense @click="hidePanel" />
    </q-card-section>
    <q-card-section>
      <div>
        Random story from your <span class="read-later">Read later</span> list:
      </div>
      <div>
        <span class="text-body1">
          <router-link
            :to="{
              path: 'read',
              query: { storyId: storyId, chapterNo: 1 },
            }"
            >{{ randomTitle }}</router-link
          >
        </span>
      </div>

      <div>Is it time to read it?</div>
    </q-card-section>
    <q-card-actions class="justify-around">
      <router-link
        :to="{
          path: 'read',
          query: { storyId: storyId, chapterNo: 1 },
        }"
      >
        <q-btn flat round color="black" icon="local_library" />
        <q-tooltip> Read now </q-tooltip>
      </router-link>
      <q-btn flat round color="pink" icon="list_alt" @click="removeFromRead">
        <q-tooltip> Remove from read later </q-tooltip>
      </q-btn>
      <q-btn flat round color="purple" icon="refresh" @click="random">
        <q-tooltip> Random story </q-tooltip>
      </q-btn>
    </q-card-actions>
  </q-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { getRandom } from "src/services/readservice";
import { addToReads } from "src/services/readservice";
import { useUserStore } from "../stores/user";
import { showNotification } from "src/utilsTS/notify";

const userStore = useUserStore();

const randomTitle = ref<string>("");
const storyId = ref<number>(0);
const visible = ref<boolean>(true);

onMounted(() => {
  random();
});

const removeFromRead = (): void => {
  addToReads(userStore.id, storyId.value)
    .then(() => {
      showNotification("Story removed from Read later", "negative");
      random();
    })
    .catch((err) => {
      console.error("Read later error:", err);
      showNotification(
        "Error occurred while adding or removing read later",
        "negative"
      );
    });
};

const random = (): void => {
  getRandom()
    .then((response) => {
      randomTitle.value = Object.values(response)[0];
      storyId.value = Number(Object.keys(response)[0]);
    })
    .catch(() => {
      visible.value = false;
    });
};

const hidePanel = () => {
  visible.value = false;
  showNotification("The random read panel will stay hidden until the page refreshes", "positive")
}

</script>

<style scoped>
.header {
  font-family: "Farro", cursive;
  color: #333 !important;
  text-transform: none;
  font-weight: 400;
}

.read-later {
  font-weight: 600;
}
</style>
