import {Component} from '@angular/core';
import {Category, Meal} from "../../models/meal";
import {CartService} from "../../shared/cart.service";
import {DataStorageService} from "../../shared/data-storage.service";
import {ToastService} from "../../shared/toast.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent {
  categories: Category[] = []
  meals: { [key: string]: Meal[] } = {};

  constructor(private cartService: CartService,
              private dataStorageService: DataStorageService,
              private toastService: ToastService) {
    this.dataStorageService.getMenu()
      .subscribe(res => {
        const meals = res;
        for (let meal of meals) {
          if (this.meals[meal.mealCategory.name]) {
            this.meals[meal.mealCategory.name].push(meal)
          } else {
            this.meals[meal.mealCategory.name] = [meal]
            this.categories.push(meal.mealCategory)
          }
        }
      })
  }
  scroll(category: string) {
    var target = document.getElementById(category);
    if (target !== null) {
      target.scrollIntoView({behavior: 'smooth', block: 'start'});
    }
  }

  addToCart(meal: Meal) {
    this.cartService.addToCart(meal);
    this.toastService.showSuccessToast("Cart", "Added item to cart")
  }

}


