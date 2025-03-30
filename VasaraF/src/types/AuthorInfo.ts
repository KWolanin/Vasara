import { StoryInfo } from "./StoryInfo";

export interface AuthorInfo {
  id: number;
  username: string;
  email: string;
  description: String;
  login: String;
  stories: StoryInfo[];
}
