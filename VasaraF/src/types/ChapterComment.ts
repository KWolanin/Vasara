
export interface ChapterComment {
  id: number;
  content: string;
  name: string;
  parentId: number;
  storyId: number;
  chapterId?: number;
  email: string;
  createdAt: string | Date;
  replies: ChapterComment[];
}

export interface ChapterPermission {
  commentAllowed: boolean;
  guestCommentAllowed: boolean;
}
