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

}
