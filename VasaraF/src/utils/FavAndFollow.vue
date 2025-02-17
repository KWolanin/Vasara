<template>
  <span>
    <q-btn push :color="favColor" flat round icon="favorite"
     class="q-pa-xs" @click="favorite">
     <q-tooltip> Add to favorites </q-tooltip>
    </q-btn>
    <q-btn push :color="followColor" flat round icon="email" class="q-pa-xs" @click="follow">
      <q-tooltip> Follow story and get an e-mail when updated </q-tooltip>
    </q-btn>
    <q-btn push :color="readColor" flat round icon="list_alt"
     class="q-pa-xs" @click="readLater">
     <q-tooltip> Add to read later </q-tooltip>
    </q-btn>
  </span>

</template>


<script setup lang="ts">
import { addToFavorite, isFav } from "src/services/favoriteservice";
import { addToFollows, isFollow } from "src/services/followservice";
import { addToReads, isReads } from "src/services/readservice";
import { Notify } from "quasar";
import { useUserStore } from "../stores/user";
import { onMounted, ref, computed } from "vue";

const userStore = useUserStore();

const props = defineProps<{
  storyId: number;
}>();

// favourite
const isFavourite = ref<Boolean>(false)

const favColor = computed(() => {
  return isFavourite.value ? "gold" : "black"
})

const favorite = (): void => {
  addToFavorite(userStore.id, props.storyId)
    .then((data) => {
      const msg = data ? "Story added to favorites" : "Story removed from favorites";
      isFavourite.value = data
      Notify.create({
        message: msg,
        position: "bottom-right",
        type: data ? "positive" : "negative",
      });
    })
    .catch((err) => {
      console.error("Favorite error:", err);
      Notify.create({
        message: "Error occurred while adding or removing favorite",
        position: "bottom-right",
        type: "negative",
      });
    });
};




// Follows
const isFollowed = ref<Boolean>(false)

const followColor = computed(() => {
  return isFollowed.value ? "purple" : "black"
})

const follow = ():void => {
  addToFollows(userStore.id, props.storyId)
    .then((data) => {
      const msg = data ? "Story followed" : "Story unfollowed";
      isFollowed.value = data
      Notify.create({
        message: msg,
        position: "bottom-right",
        type: data ? "positive" : "negative",
      });
    })
    .catch((err) => {
      console.error("Follow error:", err);
      Notify.create({
        message: "Error occurred while adding or removing follow",
        position: "bottom-right",
        type: "negative",
      });
    });
};

// read later

const isRead = ref<Boolean>(false)

const readColor = computed(() => {
  return isRead.value ? "pink" : "black"
})

const readLater = ():void => {
  addToReads(userStore.id, props.storyId)
    .then((data) => {
      const msg = data ? "Story added to Read later" : "Story removed from Read later";
      isRead.value = data
      Notify.create({
        message: msg,
        position: "bottom-right",
        type: data ? "positive" : "negative",
      });
    })
    .catch((err) => {
      console.error("Read later error:", err);
      Notify.create({
        message: "Error occurred while adding or removing read later",
        position: "bottom-right",
        type: "negative",
      });
    });
};




onMounted(() => {
  // todo check favourite, follow and to read
  isFav(userStore.id, props.storyId).then((data) => {
    isFavourite.value = data
  })
  isFollow(userStore.id, props.storyId).then((data) => {
    isFollowed.value = data
  })
  isReads(userStore.id, props.storyId).then((data) => {
    isRead.value = data
  })
})



</script>
