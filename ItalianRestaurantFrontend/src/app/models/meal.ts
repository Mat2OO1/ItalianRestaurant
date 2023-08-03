export class Meal {
  id: number
  name: string
  imgPath: string
  description: string
  price: number
  mealCategory: Category

  constructor(id: number, name: string, imgPath: string, description: string, price: number, mealCategory: Category) {
    this.id = id
    this.name = name;
    this.imgPath = imgPath;
    this.description = description;
    this.price = price;
    this.mealCategory = mealCategory;
  }

  equals(other: Meal): boolean {
    return this.name === other.name
  }
}

export class Category {
  name: string;
  imgPath: string;

  constructor(name: string, imgPath: string) {
    this.name = name;
    this.imgPath = imgPath;
  }
}
