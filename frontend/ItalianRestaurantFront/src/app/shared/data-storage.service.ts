import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CartService} from "./cart.service";
import {AuthService} from "../auth/auth.service";
import {OrderService} from "./order.service";
import {Meal, MealResponse} from "../models/meal";
import {map, Observable, tap} from "rxjs";
import {MealsService} from "./meals.service";

@Injectable()
export class DataStorageService{
  constructor(
    private http: HttpClient,
  ) {}

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
}
