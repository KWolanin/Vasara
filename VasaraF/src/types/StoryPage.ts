import { Story } from "./Story";

export interface StoryPage {
  content: Story[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}
