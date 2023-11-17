export class MealDto {
  name: string
  name_pl: string
  description: string
  description_pl: string
  price: number
  category: string
  image?: string


  constructor(name: string, name_pl: string, description: string, description_pl:string,  price: number, mealCategory: string, image?: string) {
    this.name = name;
    this.name_pl = name_pl;
    this.description = description;
    this.description_pl = description_pl;
    this.price = price;
    this.category = mealCategory;
    this.image = image;
  }
}
