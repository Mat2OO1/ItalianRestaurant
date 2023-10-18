export class MealDto{
  name: string
  image: Blob
  description: string
  price: number
  mealCategory: string


  constructor(name: string, image: Blob, description: string, price: number, mealCategory: string) {
    this.name = name;
    this.image = image;
    this.description = description;
    this.price = price;
    this.mealCategory = mealCategory;
  }
}
