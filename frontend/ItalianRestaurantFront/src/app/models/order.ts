import {Meal} from "./meal";

export class Order{
  meal: Meal
  quantity: number


  constructor(meal: Meal, quantity: number) {
    this.meal = meal;
    this.quantity = quantity;
  }
}
