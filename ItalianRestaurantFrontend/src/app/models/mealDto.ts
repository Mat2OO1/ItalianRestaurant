export class MealDto {
  name: string
  description: string
  price: number
  category: string
  image?: string


  constructor(name: string, description: string, price: number, mealCategory: string, image?: string) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.category = mealCategory;
    this.image = image;
  }
}
