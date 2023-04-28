import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CartService} from "./cart.service";
import {AuthService} from "../auth/auth.service";
import {OrderService} from "./order.service";
import {Meal} from "../models/meal";
import {tap} from "rxjs";
import {MealsService} from "./meals.service";

@Injectable()
export class DataStorageService{
  constructor(
    private http: HttpClient,
  ) {}

  getMenu(){
    return this.http
      .get<Meal[]>(
        "http://localhost:8080/meals",
      )
      .pipe(
        tap(meals => {
          return meals;
        })
      )
  }
}
