import {Meal} from "./meal";
import {Delivery} from "./delivery";
import {Table} from "./table";

export class Order {
  meal: Meal
  quantity: number

  constructor(meal: Meal, quantity: number) {
    this.meal = meal;
    this.quantity = quantity;
  }
}

export class OrderRes {
  id: number
  mealOrders: { meal: Meal, quantity: number}[]
  delivery?: Delivery
  quantity: number
  orderStatus: string
  deliveryDate?: Date
  table: Table


  constructor(id: number, mealOrders: {
    meal: Meal;
    quantity: number;
    price: number
  }[], delivery: Delivery, quantity: number, orderStatus: string, deliveryDate: Date, table: Table) {
    this.id = id;
    this.mealOrders = mealOrders;
    this.delivery = delivery;
    this.quantity = quantity;
    this.orderStatus = orderStatus;
    this.deliveryDate = deliveryDate;
    this.table = table;
  }
}

