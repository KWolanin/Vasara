const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("pages/HomeView.vue"),
  },
  {
    path: "/create",
    name: "create",
    component: () => import("src/pages/create/CreateStory.vue"),
  },
  {
    path: "/readAll",
    name: "readAll",
    component: () => import("src/read/ReadStories.vue"),
  },
  {
    path: "/mines",
    name: "mines",
    component: () => import("pages/MyStories.vue"),
  },
  {
    path: "/add",
    name: "add",
    component: () => import("src/pages/create/AddChapter.vue"),
    props: (route) => ({
      storyId: Number(route.query.storyId),
      authorId: Number(route.query.authorId),
      chapters: Number(route.query.chapters),
    }),
  },
  {
    path: "/read",
    name: "readChapter",
    component: () => import("src/read/ReadChapter.vue"),
  },
  {
    path: "/login",
    name: "login",
    component: () => import("src/pages/users/LoginPage.vue"),
  },
  {
    path: "/register",
    name: "register",
    component: () => import("src/pages/users/RegisterPage.vue"),
  },
  {
    path: "/about",
    name: "about",
    component: () => import("src/pages/about/AboutPage.vue"),
  },
  {
    path: "/manage",
    name: "manage",
    component: () => import("src/pages/create/ManageChapters.vue"),
    props: (route) => ({
      storyId: Number(route.query.storyId),
    }),
  },
  {
    path: "/:catchAll(.*)*",
    component: () => import("pages/ErrorNotFound.vue"),
  },
];

export default routes;
