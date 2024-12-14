<template>
  <div class="row justify-center q-pa-lg">
    <q-btn class="q-ma-sm btn" flat>
      <RouterLink to="/">Home</RouterLink>
    </q-btn>
    <q-btn class="q-ma-sm btn" flat>
      <RouterLink to="readAll">Read</RouterLink>
    </q-btn>
    <q-btn class="q-ma-sm btn" flat>
      <RouterLink to="mines">My stories</RouterLink>
    </q-btn>
    <q-btn class="q-ma-sm btn" flat color="grey-10">
      <RouterLink to="create">Add</RouterLink>
    </q-btn>
  </div>
</template>

<style scoped>
.btn {
  font-family: "Farro", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: #333 !important;
}

a:visited {
  color: #333;
}

.text-dark {
  color: #333 !important;
}

.bg-dark {
  background: #333 !important;
}
</style>
