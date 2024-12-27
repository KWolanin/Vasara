<template>
<div>
  <p>Edit title</p>
  <span>{{chapter.chapterTitle}}</span>
  <p>Edit position</p>
  <span>{{chapter.chapterNo}}</span>
  <p>Edit content</p>
  <p>[wip]</p>
</div>

</template>


<script setup lang="ts">

const props = defineProps({
  chapter:  {
    type: Object,
    required: true
}})


</script>
