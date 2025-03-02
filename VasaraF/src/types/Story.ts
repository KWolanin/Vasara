export interface Story {
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
  chaptersNumber: number;
  rating: string;
  comment: boolean;
  guestComment: boolean;
}
