export class MealResponse{
  id: number
  name: string
  imgPath: string
  description: string
  price: number
  mealCategory: Category

  constructor(id: number, name: string, imgPath: string, description: string, price: number, mealCategory: Category) {
    this.id = id;
    this.name = name;
    this.imgPath = imgPath;
    this.description = description;
    this.price = price;
    this.mealCategory = mealCategory;
  }

  equals(other: MealResponse): boolean {
    return this.name === other.name &&
      this.imgPath === other.imgPath &&
      this.description === other.description &&
      this.price === other.price;
  }
}

export class Meal{
  id: number
  name: string
  imgPath: string
  description: string
  price: number
  mealCategory: string

  constructor(id: number,name: string, imgPath: string, description: string, price: number, mealCategory: string) {
    this.id = id
    this.name = name;
    this.imgPath = imgPath;
    this.description = description;
    this.price = price;
    this.mealCategory = mealCategory;
  }

  equals(other: MealResponse): boolean {
    return this.name === other.name &&
      this.imgPath === other.imgPath &&
      this.description === other.description &&
      this.price === other.price;
  }
}

export class Category{
  name: string;

  constructor(name: string) {
    this.name = name;
  }
}
