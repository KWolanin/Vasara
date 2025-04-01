<template>
  <span>
    <q-btn
      v-if="showFav"
      push
      :color="favColor"
      flat
      round
      icon="favorite"
      class="q-pa-xs"
      @click="favorite"
    >
      <q-tooltip> Add to favorites </q-tooltip>
    </q-btn>
    <q-btn
      v-if="showFollow"
      push
      :color="followColor"
      flat
      round
      icon="email"
      class="q-pa-xs"
      @click="follow"
    >
      <q-tooltip> Follow story and get an e-mail when updated </q-tooltip>
    </q-btn>
    <q-btn
      v-if="showRead"
      push
      :color="readColor"
      flat
      round
      icon="list_alt"
      class="q-pa-xs"
      @click="readLater"
    >
      <q-tooltip> Add to read later </q-tooltip>
    </q-btn>
  </span>
</template>

<script setup lang="ts">
import { addToFavorite, isFav } from "src/services/favoriteservice";
import { addToFollows, isFollow } from "src/services/followservice";
import { addToReads, isReads } from "src/services/readservice";
import { showNotification } from "src/utilsTS/notify";
import { useUserStore } from "../stores/user";
import { onMounted, ref, computed } from "vue";

const userStore = useUserStore();

const props = withDefaults(
  defineProps<{
    storyId: number;
    showFav?: boolean;
    showRead?: boolean;
    showFollow?: boolean;
  }>(),
  {
    showFav: true,
    showRead: true,
    showFollow: true,
  }
);

// favourite
const isFavourite = ref<Boolean>(false);

const favColor = computed(() => {
  return isFavourite.value ? "yellow-4" : "black";
});

const favorite = (): void => {
  addToFavorite(userStore.id, props.storyId)
    .then((data) => {
      const msg = data
        ? "Story added to favorites"
        : "Story removed from favorites";
      isFavourite.value = data;
      showNotification(msg, data ? "positive" : "negative")
    })
    .catch((err) => {
      console.error("Favorite error:", err);
      showNotification("Error occurred while adding or removing favorite", "negative")
    });
};

// Follows
const isFollowed = ref<Boolean>(false);

const followColor = computed(() => {
  return isFollowed.value ? "purple" : "black";
});

const follow = (): void => {
  addToFollows(userStore.id, props.storyId)
    .then((data) => {
      const msg = data ? "Story followed" : "Story unfollowed";
      isFollowed.value = data;
      showNotification(msg, data ? "positive" : "negative")
    })
    .catch((err) => {
      console.error("Follow error:", err);
      showNotification("Error occurred while adding or removing follow", "negative")
    });
};

// read later

const isRead = ref<Boolean>(false);

const readColor = computed(() => {
  return isRead.value ? "pink" : "black";
});

const readLater = (): void => {
  addToReads(userStore.id, props.storyId)
    .then((data) => {
      const msg = data
        ? "Story added to Read later"
        : "Story removed from Read later";
      isRead.value = data;
      showNotification(msg, data ? "positive" : "negative")
    })
    .catch((err) => {
      console.error("Read later error:", err);
      showNotification("Error occurred while adding or removing read later", "negative")
    });
};

onMounted(() => {
  isFav(userStore.id, props.storyId).then((data) => {
    isFavourite.value = data;
  });
  isFollow(userStore.id, props.storyId).then((data) => {
    isFollowed.value = data;
  });
  isReads(userStore.id, props.storyId).then((data) => {
    isRead.value = data;
  });
});
</script>
