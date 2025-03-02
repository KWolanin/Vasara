import { Notify } from "quasar";

export type Status = "positive" | "warning" | "negative"

export function showNotification (message: string, type: Status) :void {
  Notify.create({
    message: message,
    position: "bottom-right",
    type: type,
  });
}
