import {Injectable} from "@angular/core";
import {Meal} from "../models/meal";

@Injectable()
export class MealsService{
  meals: Meal[] = []

}
