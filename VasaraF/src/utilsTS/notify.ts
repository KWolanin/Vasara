import { Notify } from "quasar";

export type Status = "positive" | "warning" | "negative"

export function showNotification (message: string, type: Status) :void {
  Notify.create({
    message: message,
    position: "bottom-right",
    type: type,
    color: color(type)
  });
}

const color = (type: Status) : string => {
  switch (type) {
    case "positive":
      return "green-5";
    case "negative":
      return "deep-orange-5";
    case "warning":
      return "orange-5";
    default:
      return "orange-5";
  }
}
