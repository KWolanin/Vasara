const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("pages/HomeView.vue"),
  },
  {
    path: "/create",
    name: "create",
    component: () => import("pages/CreateStory.vue"),
  },
  {
    path: "/readAll",
    name: "readAll",
    component: () => import("pages/ReadStories.vue"),
  },
  {
    path: "/mines",
    name: "mines",
    component: () => import("pages/MyStories.vue"),
  },
  {
    path: "/add",
    name: "add",
    component: () => import("pages/AddChapter.vue"),
    props: (route) => ({
      storyId: Number(route.query.storyId),
      authorId: Number(route.query.authorId),
      chapters: Number(route.query.chapters),
    }),
  },
  {
    path: "/read",
    name: "readChapter",
    component: () => import("pages/ReadChapter.vue"),
    props: (route) => ({
      storyId: Number(route.query.storyId),
      chapterNo: Number(route.query.chapterNo),
    }),
  },
  {
    path: "/:catchAll(.*)*",
    component: () => import("pages/ErrorNotFound.vue"),
  },
];

export default routes;
