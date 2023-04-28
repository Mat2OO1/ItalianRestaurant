import {Injectable} from "@angular/core";
import {Meal} from "../models/meal";
import {DataStorageService} from "./data-storage.service";

@Injectable()
export class MealsService{
  meals: Meal[] = []

  constructor(private dataStorageService: DataStorageService) {
    this.dataStorageService.getMenu()
      .subscribe(
        menu => {
          this.meals = menu;
          console.log(menu);
        }
      )
  }

}
