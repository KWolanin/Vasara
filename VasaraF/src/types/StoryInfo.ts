export interface StoryInfo {
  id: number;
  authorName: string;
  authorId: number;
  description: string | number;
  title: string | number;
  tags: string[];
  fandoms: string[];
  finished: boolean;
  publishDt: string | Date;
  updateDt: string | Date;
  chaptersTitles: string[];
  chaptersNumber: number;
  rating: string;
  comment: boolean;
  guestComment: boolean;
}
