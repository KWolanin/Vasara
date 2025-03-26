import { StoryInfo } from "./StoryInfo";

export interface StoryPage {
  content: StoryInfo[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}
