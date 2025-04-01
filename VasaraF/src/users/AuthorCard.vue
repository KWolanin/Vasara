<template>
  <q-card
    class="col-8 q-pt-sm q-pb-sm q-pr-md q-pl-md q-ma-xs card"
    :bordered="true"
  >
    <router-link
      :to="{
        path: 'user',
        query: { authorId: id },
      }"
      >{{ username }}</router-link
    >
  </q-card>
</template>

<script setup lang="ts">
defineProps<{
  username: String;
  id: number;
}>();
</script>

<style scoped></style>
