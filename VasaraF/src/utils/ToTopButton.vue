<template>
  <q-btn
    unelevated
    color="pink"
    round
    text-color="black"
    icon="arrow_circle_up"
    class="q-mr-md top-btn"
    @click="toTop"
  >
    <q-tooltip> To top </q-tooltip>
  </q-btn>
</template>

<script setup lang="ts">
const toTop = (): void => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};
</script>

<style scoped>
.top-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
  }
</style>
