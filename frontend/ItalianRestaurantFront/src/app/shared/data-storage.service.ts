import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CartService} from "./cart.service";
import {AuthService} from "../auth/auth.service";
import {OrderService} from "./order.service";
import {Meal, MealResponse} from "../models/meal";
import {interval, map, Observable, Subject, tap} from "rxjs";
import {MealsService} from "./meals.service";
import {Delivery} from "../models/delivery";

@Injectable()
export class DataStorageService{
  orderDetails: Subject<{date: string, status: string}> = new Subject()

  constructor(private http: HttpClient) {
    this.getOrderDetails()
    this.orderDetails.subscribe(
      (res) => {
        console.log(res)
      }
    )
  }

  getMenu(){
    return this.http
      .get<MealResponse[]>(
        "http://localhost:8080/meals",
      )
      .pipe(
        map(meals => meals.map(meal => new Meal(meal.name,meal.imgPath,meal.description,meal.price,meal.mealCategory.name)))
      )
  }

  getCategories(): Observable<string[]> {
    return this.http
      .get<{id: number, category: string}[]>("http://localhost:8080/meal-categories")
      .pipe(
        map(categories => categories.map(c => c.category))
      );
  }

  makeAnOrder(delivery: Delivery, order: {meal: Meal,quantity: number, price: number}[]){
    return this.http
      .post("http://localhost:8080/order",
        {
          delivery: delivery,
          mealOrders: order
        })
  }

  getOrderDetails(){
    interval(1000).subscribe(() => {
      return this.http
        .get("http://localhost:8080/order/user")
        .subscribe(
          (res: any) => {
            this.orderDetails.next({date: res[0].orderDate, status: res[0].orderStatus})
          }
        )
    }
    )
  }
}
