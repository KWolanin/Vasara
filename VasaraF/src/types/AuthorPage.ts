import { AuthorInfo } from "./AuthorInfo";

export interface AuthorPage {
  content: AuthorInfo[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}
