import {Meal} from "../models/meal";
import {Subject} from "rxjs";

export class OrderService{
  order: {id: number, cart: {meal: Meal,quantity: number}[]}
  orderStatus: Subject<string>;
  constructor() {
    this.order =
      {id: 1, cart:
          [
            {meal: new Meal('Carbonara',
          'http://kuchnia-domowa.pl/images/content/176/spaghetti-carbonara.jpg',
          'grana padano, pasta, basil', 6.99, 'grana'), quantity: 3}
          ]
      }
      this.orderStatus = new Subject()
      setInterval(() =>{
        this.orderStatus.next("In preperation")

      }, 100)
  }
}
