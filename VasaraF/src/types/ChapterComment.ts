export interface ChapterComment {
  id: number;
  content: string;
  name: string;
  parentId: number;
  storyId: number;
  chapterId?: number;
  email: string;
  createdAt: string | Date;
}

export interface ChapterPermission {
  commentAllowed: boolean;
  guestCommentAllowed: boolean;
}
