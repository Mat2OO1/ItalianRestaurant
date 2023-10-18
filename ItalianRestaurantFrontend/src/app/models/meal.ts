import {CategoryDto} from "./categoryDto";

export class Meal {
  id: number
  name: string
  image: string
  description: string
  price: number;
  mealCategory: CategoryDto

  constructor(id: number, name: string, image: string, description: string, price: number, mealCategory: CategoryDto) {
    this.id = id
    this.name = name;
    this.image = image;
    this.description = description;
    this.price = price;
    this.mealCategory = mealCategory;
  }

}
