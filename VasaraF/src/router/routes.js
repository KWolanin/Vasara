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
    meta: { requiresAuth: true }
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
    meta: { requiresAuth: true }
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
    meta: { requiresAuth: true }
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
    meta: { requiresAuth: true }
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
    meta: { requiresAuth: true }
  },
  {
    path: "/account",
    name: "Account",
    component: () => import("src/users/ManageAccount.vue"),
    meta: { requiresAuth: true }
  },
  {
    path: "/favs",
    name: "Favorites and following",
    component: () => import("src/story/FavoritesAndFollowing.vue"),
    meta: { requiresAuth: true }
  },
  {
    path: "/user",
    name: "User profile",
    component: () => import("src/users/UserPage.vue"),
    props: (route) => ({
      authorId: Number(route.query.authorId),
    }),
  },
  {
    path: "/:catchAll(.*)*",
    component: () => import("src/ErrorNotFound.vue"),
  },
];

export default routes;
