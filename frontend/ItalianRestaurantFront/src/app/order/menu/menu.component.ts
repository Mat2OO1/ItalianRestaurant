import {Component, ElementRef, OnInit} from '@angular/core';
import {Category, Meal} from "../../models/meal";
import {CartService} from "../../shared/cart.service";
import {MealsService} from "../../shared/meals.service";
import {DataStorageService} from "../../shared/data-storage.service";
@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent{
  categories: Category[] = []
  meals: {[key:string]: Meal[]} = {};
  constructor(private cartService: CartService,
              private dataStorageService: DataStorageService) {
    this.dataStorageService.getMenu()
      .subscribe(res => {
        const meals = res;
        for(let meal of meals){
          if(this.meals[meal.mealCategory.name]){
            this.meals[meal.mealCategory.name].push(meal)
          }
          else{
            this.meals[meal.mealCategory.name] = [meal]
            this.categories.push(meal.mealCategory)
          }
        }
      })
  }

  // meals: {[key:string]: Meal[]} = {
  //   'Pasta Dishes': [new Meal('Carbonara','http://kuchnia-domowa.pl/images/content/176/spaghetti-carbonara.jpg','grana padano, pasta, basil', 6.99),
  //     new Meal('Aglio Oglio','https://italia-by-natalia.pl/wp-content/uploads/2020/11/aglio-olio-e-peperoncino.jpg','olive oil, garlic, pasta', 10.49)],
  //   'Pizza': [new Meal("Margherita", '', 'tomato sauce, cheese', 2.49)],
  //   'Seafood': [new Meal('Clams', '','some description', 15.99)]
  // }

  scroll(category: string){
    var target = document.getElementById(category);
    if(target !== null){
      target.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }

  addToCart(meal:Meal){
    this.cartService.addToCart(meal);
  }

}


