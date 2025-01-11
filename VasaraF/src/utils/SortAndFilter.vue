<template>
<q-card class="col-7 q-pa-lg card" flat>
  <div class="search">
  <q-input outlined v-model="criteria.title" label="Title"  />
  <q-input outlined v-model="criteria.author" label="Author" />
  <q-input outlined v-model="criteria.description" label="Desciption" />
  <tag-input v-model="criteria.fandoms" label="Fandom(s)" />
  <tag-input v-model="criteria.tags" label="Tag(s)" />
  </div>
  <div>

  </div>
</q-card>

<q-btn-dropdown color="gold"
 label="Sort by" size="xs" text-color="black" class="q-ml-md card">
      <q-list>
        <q-item clickable v-close-popup @click="onItemClick">
          <q-item-section>
            <q-item-label>Update date (newest)</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-close-popup @click="onItemClick">
          <q-item-section>
            <q-item-label>Update date (oldest)</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-close-popup @click="onItemClick">
          <q-item-section>
            <q-item-label>Author (A-Z)</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-close-popup @click="onItemClick">
          <q-item-section>
            <q-item-label>Author (Z-A)</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-close-popup @click="onItemClick">
          <q-item-section>
            <q-item-label>Story title (A-Z)</q-item-label>
          </q-item-section>
        </q-item>
        <q-item clickable v-close-popup @click="onItemClick">
          <q-item-section>
            <q-item-label>Story title (Z-A)</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-btn-dropdown>
</template>

<script setup lang="ts">
import { reactive, watch, defineEmits } from "vue"
import { Criteria } from "../types/Criteria"
import TagInput from "./TagInput.vue";

const emit = defineEmits();

const criteria = reactive<Criteria>({
  title: "",
  author: "",
  fandoms: [],
  tags: [],
  description: ""
})

let timeout: NodeJS.Timeout | null = null

watch(criteria, () => {
  if (timeout) {
    clearTimeout(timeout)
  }
  timeout = setTimeout(() => {
    emit("criteria-changed", criteria)
  }, 100)
})

const onItemClick = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  emit('sort-changed', target.innerText);
};

</script>


<style scoped>

.search {
  display: flex;
  gap: 10px;
}

</style>
