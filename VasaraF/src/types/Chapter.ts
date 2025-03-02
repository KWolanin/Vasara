import { Story } from "./Story";

export interface Chapter {
  chapterTitle: string | number;
  content: string;
  authorId: number;
  storyId: number;
  chapterNo: number;
  id: number;
  published: string | number;
  updated: string | number;
  storyDTO: Story
}
