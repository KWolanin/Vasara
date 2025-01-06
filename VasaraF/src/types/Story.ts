export interface Story {
  id: number;
  authorName: string;
  authorId: number;
  description: string;
  title: string;
  tags: string[];
  fandoms: string[];
  finished: boolean;
  publishDt: string | Date;
  updateDt: string | Date;
  chaptersNumber: number;
}
