<template>
  <main-menu />
  <div div class="row justify-center q-pa-lg">
    <q-card class="col-8 q-pa-lg card" flat>
      <q-tabs v-model="tab" class="text-teal">
        <q-tab
          name="fav"
          icon="favorite"
          label="Favorite stories"
          class="text-gold"
        />
        <q-tab
          name="mail"
          icon="mail"
          label="Following stories"
          class="text-purple"
        />
        <q-tab
          name="read"
          icon="list_alt"
          label="To read later"
          class="text-pink"
        />
        <q-tab
          name="authors"
          icon="mail"
          label="Followed authors"
          class="text-purple"
        />
      </q-tabs>
      <q-separator />
      <q-tab-panels v-model="tab" animated>
        <q-tab-panel name="fav">
          <fav-list />
        </q-tab-panel>
        <q-tab-panel name="mail">
          <follow-list />
        </q-tab-panel>
        <q-tab-panel name="read">
          <read-later-list />
        </q-tab-panel>
        <q-tab-panel name="authors">
          <follow-author-list />
        </q-tab-panel>
      </q-tab-panels>
    </q-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import FavList from "./favs/FavList.vue";
import FollowList from "./favs/FollowList.vue";
import ReadLaterList from "./favs/ReadLaterList.vue";
import FollowAuthorList from "./favs/FollowAuthorList.vue";

const tab = ref<string | number>("fav");
</script>

<style scoped>
:deep(.q-tab__label) {
  color: black !important;
  font-family: "Farro", sans-serif;
}
</style>
