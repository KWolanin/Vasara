<template>
  <div class="q-mb-md chapter">
    <h4 class="title">{{ data.chapterTitle }}</h4>
    <h6 class="description">
      Chapter {{data.chapterNo}} of <strong>{{ data.storyDTO.title }}</strong>
      by
      <router-link
      :to="{
        path: 'user',
        query: { authorId: data.storyDTO.authorId },
      }"
    >{{ data.storyDTO.authorName }}</router-link>
  </h6>
    <div class="content-wrapper">
      <q-card class="q-pa-md q-mb-lg content" flat>
        <div
          class="paragraph-container"
          v-for="paragraph in data.paragraphs"
          :key="paragraph.id"
          :style="{ fontSize: fontSize + 'px' }"
          @mouseover="$emit('hover-paragraph', paragraph.id)"
          @mouseleave="$emit('clear-hover')"
          @mouseenter="$emit('save-progress', paragraph.id)"
          :id="'paragraph-' + paragraph.id"
        >
          <!-- <q-btn // DON'T REMOVE: needed to rework the comment system in the future
              v-if="hoveredParagraph === paragraph.id"
              icon="add_comment"
              flat
              dense
              size="sm"
              class="comment-btn"
              @mouseover="setHovered(paragraph.id)"
              @mouseleave="clearHovered"
            /> -->
          <p class="paragraph" v-html="sanitized(paragraph.content)"></p>
        </div>
        <q-spinner-hearts v-if="isLoading" size="50px" color="yellow-4" />
      </q-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import DOMPurify from "dompurify";

defineProps({
  data: Object,
  fontSize: Number,
  isLoading: Boolean,
});

const sanitized = (html: string): string => DOMPurify.sanitize(html);
</script>

<style scoped>
.chapter {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 0;
  margin: 0 auto;
  flex-wrap: wrap;
}

.content {
  width: 80%;
  will-change: transform;
}

.content-wrapper {
  width: 100%;
  display: flex;
  justify-content: center;
}

.paragraph-container {
  display: flex;
  align-items: center;
  position: relative;
}

.paragraph {
  margin: 0 0 1px;
  width: 100%;
}

.comment-btn {
  position: absolute;
  right: -30px;
  transition: opacity 0.2s ease-in-out;
}

::v-deep(.paragraph hr) {
  border: none;
  height: 1px;
  background: linear-gradient(to right, transparent, #ccc, transparent);
  margin: 1rem 0;
  width: 100%;
}

.title {
  margin-bottom: 5px;
}

.description {
  margin-top: 5px;
}

</style>
