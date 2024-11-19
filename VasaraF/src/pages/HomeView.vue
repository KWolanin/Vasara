<template>
  <aside>
    <q-btn>
      <RouterLink to="create">
        <h2>Create a story</h2>
      </RouterLink>
    </q-btn>
    <q-btn>
      <RouterLink to="readAll">
        <h2>Read stories</h2>
      </RouterLink>
    </q-btn>
  </aside>
</template>

<script setup>
import { RouterLink } from "vue-router";
</script>
