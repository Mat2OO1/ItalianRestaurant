import {CategoryDto} from "./categoryDto";

export class Meal {
  id: number
  name: string
  imgPath: string
  description: string
  price: number;
  mealCategory: CategoryDto

  constructor(id: number, name: string, imgPath: string, description: string, price: number, mealCategory: CategoryDto) {
    this.id = id
    this.name = name;
    this.imgPath = imgPath;
    this.description = description;
    this.price = price;
    this.mealCategory = mealCategory;
  }

}
