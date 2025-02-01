<template>
  <q-card class="col-8 q-pa-lg q-ma-xs card" flat :bordered="bordered">
    <div class="row no-wrap items-center q-mt-sm">
    <q-chip
      v-for="(fandom, index) in story.fandoms"
      :key="index"
      class="chip bg-accent-purple"
      >{{ fandom }}</q-chip
    >
    <q-space/>
    <q-chip v-if="!story.finished" class="bg-accent-gold chip">🔓 in progress</q-chip>
    <q-chip v-else class="bg-accent-gold chip">🔐 completed</q-chip>
    </div>
    <div>
      <q-chip
      v-for="(tag, index) in story.tags"
      :key="index"
      class="chip bg-accent-pink"
      >{{ tag }}</q-chip
    >
    </div>
    <span class="text-h4 q-mb-md">
      {{ story.title }}
    </span>
    <span class="text-h5"> by {{ story.authorName }}</span>
    <div class="q-mt-sm q-mb-sm text-caption">
      <strong>published: </strong> {{ formatDate(story.publishDt.toString()) }}
    </div>
    <span class="q-mt-sm q-mb-sm text-caption">
      <strong> last update: </strong>{{ formatDate(story.updateDt.toString()) }}</span
    >
    <p class="text-body1 q-mt-sm">
      {{ story.description }}
    </p>
    <div class="text-h6 q-mb-sm">
      Chapters {{ story.chaptersNumber }} /
      {{ !story.finished ? "?" : story.chaptersNumber }}
      <q-tooltip v-if="story.chaptersNumber === 0">
        This story has no chapters and it is not visible for other users
      </q-tooltip>
    </div>
    <q-separator inset />

    <div class="row no-wrap items-center">
    <q-btn
      :disabled="story.chaptersNumber == 0"
      @click="readChapter"
      class="q-my-md btn"
      flat
      >Read</q-btn
    >
    <q-space/>
    <slot name="tools"></slot>
    <slot name="following"></slot>
    <slot name="removeFollowing"></slot>
    </div>

  </q-card>
</template>

<script setup lang="ts">
import { format } from "date-fns";
import { Story } from "src/types/Story";
import { useRouter } from "vue-router";
const router = useRouter();

const props = defineProps<{
  story: Story;
  bordered?: boolean;
}>();

const formatDate = (date: string) : string => {
  return format(new Date(date), "dd.MM.yyyy");
};

const readChapter = () : void => {
  router.push({
    path: "/read",
    query: {
      storyId: props.story.id,
      chapterNo: 1,
    },
  });
};
</script>

<style scoped>

.chip {
  border-radius: 3px !important;
}

img {
  width: 50px;
}
</style>
