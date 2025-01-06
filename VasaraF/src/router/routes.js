const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("src/HomeView.vue"),
  },
  {
    path: "/create",
    name: "create",
    component: () => import("src/story/CreateStory.vue"),
  },
  {
    path: "/readAll",
    name: "readAll",
    component: () => import("src/story/ReadStories.vue"),
  },
  {
    path: "/mines",
    name: "mines",
    component: () => import("src/story/MyStories.vue"),
  },
  {
    path: "/add",
    name: "add",
    component: () => import("src/chapter/AddEditChapter.vue"),
    props: (route) => ({
      storyId: Number(route.query.storyId),
      authorId: Number(route.query.authorId),
      chapters: Number(route.query.chapters),
    }),
  },
  {
    path: "/edit",
    name: "edit",
    component: () => import("src/chapter/AddEditChapter.vue"),
    props: (route) => ({
      storyId: Number(route.query.storyId),
      authorId: Number(route.query.authorId),
      chapters: Number(route.query.chapters),
    }),
  },
  {
    path: "/read",
    name: "readChapter",
    component: () => import("src/chapter/ReadChapter.vue"),
  },
  {
    path: "/login",
    name: "login",
    component: () => import("src/users/LoginPage.vue"),
  },
  {
    path: "/register",
    name: "register",
    component: () => import("src/users/RegisterPage.vue"),
  },
  {
    path: "/about",
    name: "about",
    component: () => import("src/AboutPage.vue"),
  },
  {
    path: "/manage",
    name: "manage",
    component: () => import("src/chapter/ManageChapters.vue"),
    props: (route) => ({
      storyId: Number(route.query.storyId),
    }),
  },
  {
    path: "/:catchAll(.*)*",
    component: () => import("src/ErrorNotFound.vue"),
  },
];

export default routes;
