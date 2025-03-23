import { Story } from "./Story";

export interface Chapter {
  chapterTitle: string | number;
  content: string;
  paragraphs: Paragraph[] | null;
  authorId: number;
  storyId: number;
  chapterNo: number;
  id: number;
  published: string | number;
  updated: string | number;
  storyDTO: Story;
  next: boolean;
  previous: boolean;
}

export interface Paragraph {
  content: string;
  id: number;
}
