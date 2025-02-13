import { Story } from "./Story";

export interface Author {
  id: number;
  username: string;
  email: string;
  stories?: Story[];
  description?: string;
}
