import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Meal, MealResponse} from "../models/meal";
import {interval, map, Observable, Subject, tap} from "rxjs";
import {Delivery} from "../models/delivery";

@Injectable()
export class DataStorageService{
  constructor(private http: HttpClient) {}

  getMenu(){
    return this.http
      .get<MealResponse[]>(
        "http://localhost:8080/meals",
      )
      .pipe(
        map(meals => meals.map(meal => new Meal(meal.id,meal.name,meal.imgPath,meal.description,meal.price,meal.mealCategory.name)))
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

}
