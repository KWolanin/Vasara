<template>
  <main-menu />
  <div>wip, manage for story {{ route.query.storyId }}</div>
</template>

<script setup>
import MainMenu from "../../components/MainMenu.vue";
import { useRoute } from "vue-router";
const route = useRoute();
</script>
