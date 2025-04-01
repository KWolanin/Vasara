<template>
  <q-card class="col-7 q-pa-lg card" flat>
    <div class="q-ma-sm ask flex justify-center">
      What do you want to read today?
    </div>
    <div class="search flex justify-center col-6">
      <q-input
        outlined
        v-model="criteria.title"
        label="Title"
        color="burgund"
      />
      <q-input
        outlined
        v-model="criteria.author"
        label="Author"
        color="burgund"
      />
      <q-input
        outlined
        v-model="criteria.description"
        label="Desciption"
        color="burgund"
      />
      <tag-input v-model="criteria.fandoms" label="Fandom(s)" />
      <tag-input v-model="criteria.tags" label="Tag(s)" />
      <q-select
        class="custom-width"
        v-model="criteria.rating"
        :options="ratingOptions"
        label="Rating"
        outlined
        clearable
        color="burgund"
        placeholder="Rating"
        stack-label
      />
      <q-btn-dropdown
        color="yellow-4"
        label="Sort by"
        size="xs"
        text-color="black"
        class="q-ml-md q-mb-md card"
        unelevated
      >
        <q-list>
          <q-item clickable v-close-popup @click="sort">
            <q-item-section>
              <q-item-label>Update date (newest)</q-item-label>
            </q-item-section>
          </q-item>

          <q-item clickable v-close-popup @click="sort">
            <q-item-section>
              <q-item-label>Update date (oldest)</q-item-label>
            </q-item-section>
          </q-item>

          <q-item clickable v-close-popup @click="sort">
            <q-item-section>
              <q-item-label>Author (A-Z)</q-item-label>
            </q-item-section>
          </q-item>

          <q-item clickable v-close-popup @click="sort">
            <q-item-section>
              <q-item-label>Author (Z-A)</q-item-label>
            </q-item-section>
          </q-item>

          <q-item clickable v-close-popup @click="sort">
            <q-item-section>
              <q-item-label>Story title (A-Z)</q-item-label>
            </q-item-section>
          </q-item>
          <q-item clickable v-close-popup @click="sort">
            <q-item-section>
              <q-item-label>Story title (Z-A)</q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </q-btn-dropdown>
    </div>
  </q-card>
</template>

<script setup lang="ts">
import { reactive, watch } from "vue";
import { Criteria } from "../types/Criteria";
import TagInput from "./TagInput.vue";

const emit = defineEmits(["criteria-changed", "sort-changed"]);

const criteria = reactive<Criteria>({
  title: "",
  author: "",
  fandoms: [],
  tags: [],
  description: "",
  rating: "",
});

let timeout: NodeJS.Timeout | null = null;

watch(criteria, () => {
  if (timeout) {
    clearTimeout(timeout);
  }
  timeout = setTimeout(() => {
    emit("criteria-changed", criteria);
  }, 100);
});

const sort = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  emit("sort-changed", target.innerText);
};

const ratingOptions = ["KIDS", "TEEN", "ADULT", "MATURE"];
</script>

<style scoped>
.search {
  display: flex;
  gap: 10px;
}

.ask {
  font-family: "Farro", cursive;
  color: #333 !important;
  font-weight: 500;
  font-size: 1.5rem;
}

.custom-width {
  width: 25%;
}
</style>
