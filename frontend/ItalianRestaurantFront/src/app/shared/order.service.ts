import {Meal} from "../models/meal";

export class OrderService{
  order: {id: number, cart: {meal: Meal,quantity: number}[]}

  constructor() {
    this.order =
      {id: 1, cart:
          [
            {meal: new Meal('Carbonara',
          'http://kuchnia-domowa.pl/images/content/176/spaghetti-carbonara.jpg',
          'grana padano, pasta, basil', 6.99), quantity: 3}
          ]
      }
  }
}
