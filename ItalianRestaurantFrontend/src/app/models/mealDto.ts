export class MealDto{
  name: string
  img: Blob
  description: string
  price: number
  mealCategory: string


  constructor(name: string, img: Blob, description: string, price: number, mealCategory: string) {
    this.name = name;
    this.img = img;
    this.description = description;
    this.price = price;
    this.mealCategory = mealCategory;
  }
}
