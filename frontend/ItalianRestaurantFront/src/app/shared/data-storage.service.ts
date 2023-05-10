import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Meal} from "../models/meal";
import {interval, map, Observable, Subject, tap} from "rxjs";
import {Delivery} from "../models/delivery";
import {Order, OrderRes} from "../models/order";

@Injectable()
export class DataStorageService{
  constructor(private http: HttpClient) {}

  getMenu(){
    return this.http
      .get<Meal[]>(
        "http://localhost:8080/meals",
      )
      .pipe(
        map(meals => meals.map(meal => new Meal(meal.id,meal.name,meal.imgPath,meal.description,meal.price,meal.mealCategory)))
      )
  }

  getCategories() {
    return this.http
      .get<{name: string, imgPath: string}[]>("http://localhost:8080/meal-categories")
  }

  makeAnOrder(delivery: Delivery, order: {meal: Meal,quantity: number, price: number}[]){
    return this.http
      .post("http://localhost:8080/order",
        {
          delivery: delivery,
          mealOrders: order,
          orderStatus: "IN_PREPARATION"
        })
  }

  getOrders(){
    return this.http
      .get<OrderRes[]>("http://localhost:8080/order/all")
  }

}
