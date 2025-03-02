
export interface ChapterComment {
  id: number;
  content: string | number;
  name: string | number;
  parentId: number;
  storyId: number;
  chapterId?: number;
  email: string | number;
  createdAt: string | Date;
  replies: ChapterComment[];
}

export interface ChapterPermission {
  commentAllowed: boolean;
  guestCommentAllowed: boolean;
}
