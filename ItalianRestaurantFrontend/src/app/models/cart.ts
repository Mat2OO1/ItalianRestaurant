import {Meal} from "./meal";

export class Cart {
  meals: { meal: Meal, quantity: number, price: number }[] = []
  table?: number;
}
