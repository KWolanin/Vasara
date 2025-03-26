import { StoryInfo } from "./StoryInfo";

export interface Author {
  id: number;
  username: string;
  email: string;
  stories?: StoryInfo[];
  description?: string;
}
