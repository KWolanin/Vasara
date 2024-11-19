<template>
  <div>
    <q-input
      v-model="tagInput"
      :label="label"
      @keydown.enter.prevent="addTag"
      outlined
      dense
      class="q-mb-md"
    />
    <div>
      <q-chip
        v-for="(tag, index) in tags"
        :key="index"
        removable
        @remove="removeTag(index)"
        class="q-mr-xs"
      >
        {{ tag }}
      </q-chip>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => [],
  },
  label: {
    type: String,
    default: "Tag(i)",
  },
});

const emit = defineEmits(["update:modelValue"]);

const tagInput = ref("");
const tags = ref([...props.modelValue]);

function addTag() {
  const trimmedTag = tagInput.value.trim();
  if (trimmedTag && !tags.value.includes(trimmedTag)) {
    tags.value.push(trimmedTag);
    tagInput.value = "";
    emit("update:modelValue", tags.value);
  }
}

function removeTag(index) {
  tags.value.splice(index, 1);
  emit("update:modelValue", tags.value);
}

watch(
  () => props.modelValue,
  (newValue) => {
    tags.value = [...newValue];
  }
);
</script>

<style scoped>
.q-chip {
  cursor: pointer;
}
</style>
