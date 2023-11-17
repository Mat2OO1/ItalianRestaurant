import {CategoryDto} from "./categoryDto";

export class Meal {
  id: number
  name: string
  name_pl: string
  image: string
  description: string
  description_pl: string
  price: number
  mealCategory: CategoryDto

  constructor(id: number, name: string, name_pl:string, image: string, description: string, description_pl:string, price: number, mealCategory: CategoryDto) {
    this.id = id
    this.name = name;
    this.name_pl = name_pl;
    this.image = image;
    this.description = description;
    this.description_pl = description_pl;
    this.price = price;
    this.mealCategory = mealCategory;
  }

}
