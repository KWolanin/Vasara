import { route } from "quasar/wrappers";
import {
  createRouter,
  createMemoryHistory,
  createWebHistory,
  createWebHashHistory,
} from "vue-router";
import routes from "./routes";
import { useUserStore } from "src/stores/user";
import { computed } from 'vue'

export default route(function () {
  const userStore = useUserStore();
  const isLoggedIn = computed(() => !!userStore.id);

  const createHistory = process.env.SERVER
    ? createMemoryHistory
    : process.env.VUE_ROUTER_MODE === "history"
    ? createWebHistory
    : createWebHashHistory;

  const Router = createRouter({
    scrollBehavior: () => ({ left: 0, top: 0 }),
    routes,
    history: createHistory(process.env.VUE_ROUTER_BASE),
  });

  Router.beforeEach((to, from, next) => {
    if (to.meta.requiresAuth && !isLoggedIn.value) {
      next({ name: 'login' });
    } else {
      next();
    }
  });

  return Router;
});
