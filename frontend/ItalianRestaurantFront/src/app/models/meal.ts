export class Meal{
  name: string
  imgPath: string
  description: string
  price: number
  constructor(name: string,
              imgPath: string,
              description: string,
              price: number) {
    this.name = name
    this.imgPath = imgPath
    this.description = description
    this.price = price
  }
  equals(other: Meal): boolean {
    return this.name === other.name &&
      this.imgPath === other.imgPath &&
      this.description === other.description &&
      this.price === other.price;
  }



}
