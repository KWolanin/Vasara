import { StoryInfo } from "./StoryInfo";

export interface AuthorInfo {
  id: number;
  username: string;
  email: string;
  description: string;
  login: string;
  stories: StoryInfo[];
}
