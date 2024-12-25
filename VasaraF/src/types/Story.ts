export interface Story {
  id: number;
  authorName: string;
  authorId: number;
  description: string;
  title: string;
  tags: string[];
  fandoms: string[];
  finished: boolean;
  publishDt: string;
  updateDt: string;
  chaptersNumber: number;
}