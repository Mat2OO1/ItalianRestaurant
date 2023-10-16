import {Component, OnInit} from '@angular/core';
import {Meal} from "../../models/meal";
import {CartService} from "../../shared/cart.service";
import {DataStorageService} from "../../shared/data-storage.service";
import {ToastService} from "../../shared/toast.service";
import {CategoryDto} from "../../models/categoryDto";
import {AuthService} from "../../authentication/auth/auth.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})

export class MenuComponent implements OnInit {
  categories: CategoryDto[] = []
  meals: { [key: string]: Meal[] } = {};
  currentPage = 0
  totalPages = 0

  constructor(private cartService: CartService,
              private dataStorageService: DataStorageService,
              private toastService: ToastService,
              private authService: AuthService) {

          this.authService.user.subscribe(
            (user) => {
              this.isLoggedIn = !!user.token;
            }
          )
  }

  isLoggedIn = false;


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

  getCurrentCategories() {
    return this.categories.filter(cat => Object.keys(this.meals).includes(cat.name))
  }

  toNextPage() {
    window.scrollTo({top: 0});
    if (this.currentPage + 1 < this.totalPages) {
      setTimeout(
        () => this.dataStorageService.nextPage(), 500)
    }
  }


  toPreviousPage() {
    window.scrollTo({top: 0});
    if (this.currentPage >= 1) {
      setTimeout(
        () => this.dataStorageService.previousPage(), 500)
    }
  }


}


