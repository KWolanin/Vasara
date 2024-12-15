import { useUserStore } from "src/stores/user";

export default async ({ store }) => {
  const userStore = useUserStore(store);
  userStore.loadUser();
};
