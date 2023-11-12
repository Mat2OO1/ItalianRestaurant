import {Meal} from "./meal";

export class MealResponse{
  content: Meal[];
  totalPages: number;
  totalElements: number;
  number: number


  constructor(content: Meal[], totalPages: number, totalElements: number, number: number) {
    this.content = content;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
    this.number = number;
  }
}

export interface MealsWithPagination{
  meals: Meal[]
  numOfPages: number
  currPage: number
}
