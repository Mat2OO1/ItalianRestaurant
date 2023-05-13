import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Meal} from "../models/meal";
import {map} from "rxjs";
import {Delivery} from "../models/delivery";
import {OrderRes} from "../models/order";
import {environment} from "../../environments/environment";

@Injectable()
export class DataStorageService {
  constructor(private http: HttpClient) {
  }

  getMenu() {
    return this.http
      .get<Meal[]>(
        `${environment.apiUrl}/meals`,
      )
      .pipe(
        map(meals => meals.map(meal => new Meal(meal.id, meal.name, meal.imgPath, meal.description, meal.price, meal.mealCategory)))
      )
  }

  getCategories() {
    return this.http
      .get<{ name: string, imgPath: string }[]>(`${environment.apiUrl}/meal-categories`)
  }

  makeAnOrder(delivery: Delivery, order: { meal: Meal, quantity: number, price: number }[]) {
    return this.http
      .post(`${environment.apiUrl}/order`,
        {
          delivery: delivery,
          mealOrders: order,
          orderStatus: "IN_PREPARATION"
        })
  }

  getOrders() {
    return this.http
      .get<OrderRes[]>(`${environment.apiUrl}/order/all`)
  }

  updateOrder(orderStatus: string, deliveryDate: string, orderId: number) {
    return this.http
      .post(`${environment.apiUrl}/order/change-status`, {
        orderStatus: orderStatus,
        deliveryDate: deliveryDate,
        orderId: orderId
      })
      .subscribe(
        (res) => {
          console.log(res)
        }
      )

  }

}
