import {Component, OnInit} from '@angular/core';
import {Category, Meal} from "../../models/meal";
import {CartService} from "../../shared/cart.service";
import {DataStorageService} from "../../shared/data-storage.service";
import {ToastService} from "../../shared/toast.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit{
  categories: Category[] = []
  meals: { [key: string]: Meal[] } = {};
  currentPage = 0
  totalPages = 0

  constructor(private cartService: CartService,
              private dataStorageService: DataStorageService,
              private toastService: ToastService) {}
  ngOnInit(): void {
    this.dataStorageService.getCategories()
      .subscribe(
        res => {
          this.categories = res
        }
      )
    this.dataStorageService.meals
      .subscribe(res => {
        this.meals = {}
        this.totalPages = res.numOfPages
        this.currentPage = res.currPage
        for (let meal of res.meals) {
          if (this.meals[meal.mealCategory.name]) {
            this.meals[meal.mealCategory.name].push(meal)
          } else {
            this.meals[meal.mealCategory.name] = [meal]
          }
        }
      })

    this.dataStorageService.getMeals();
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

  getCurrentCategories(){
    return this.categories.filter(cat => Object.keys(this.meals).includes(cat.name) )
  }

  toNextPage(){
    this.dataStorageService.nextPage()
  }

  toPreviousPage(){
    this.dataStorageService.previousPage()

  }



}


