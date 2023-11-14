import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {Meal} from "../../models/meal";
import {CartService} from "../../shared/cart.service";
import {DataStorageService} from "../../shared/data-storage.service";
import {ToastService} from "../../shared/toast.service";
import {CategoryDto} from "../../models/categoryDto";
import {AuthService} from "../../authentication/auth/auth.service";
import {Subscription} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {Category} from "../../models/category";
import {MatPaginator, PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})

export class MenuComponent implements OnInit, OnDestroy {
  categories: Category[] = []
  meals: Meal[] = [];
  totalPages = 0
  pageIndex = 0
  size = 5;
  mealsNumber = 0;
  isLoggedIn = false;
  filteredCategory ?: Category;


  authSubscription?: Subscription | null;

  constructor(private cartService: CartService,
              private dataStorageService: DataStorageService,
              private toastService: ToastService,
              private authService: AuthService,
              private activatedRoute: ActivatedRoute) {
    this.authSubscription = this.authService.user.subscribe(
      (user) => {
        this.isLoggedIn = !!user?.token;
      }
    );
    this.sentMealsRequest();
  }


  ngOnInit(): void {
    if (this.activatedRoute.snapshot.queryParams['payment'] === 'failed') {
      this.toastService.showErrorToast('Payment', 'Payment failed. Order not placed. Please try again.')
    }
    if (this.activatedRoute.snapshot.queryParams['table']) {
      this.cartService.addTable(this.activatedRoute.snapshot.queryParams['table'])
      this.toastService.showSuccessToast('Table', 'You are ordering to table ' + this.activatedRoute.snapshot.queryParams['table'])
    }
    this.dataStorageService.getCategories()
      .subscribe(
        res => {
          this.categories = res
        }
      )
    this.sentMealsRequest();
  }

  sentMealsRequest(category?: string) {
    if (category) {
      this.dataStorageService.getFilteredMeals(this.pageIndex, this.size, category)
        .subscribe(
          (res) => {
            this.totalPages = res.totalPages
            this.mealsNumber = res.totalElements
            this.pageIndex = res.number
            this.meals = res.content.map(meal => new Meal(meal.id, meal.name, meal.image, meal.description, meal.price, meal.mealCategory))
          }
        )
    }
    else{
      this.dataStorageService.getMeals(this.pageIndex, this.size)
        .subscribe(
          (res) => {
            this.totalPages = res.totalPages
            this.mealsNumber = res.totalElements
            this.pageIndex = res.number
            this.meals = res.content.map(meal => new Meal(meal.id, meal.name, meal.image, meal.description, meal.price, meal.mealCategory))
          }
        )
    }
  }

  onCategoryChange(category: Category) {
    this.pageIndex = 0;
    if (this.filteredCategory !== undefined && this.filteredCategory.name === category.name) {
      this.filteredCategory = undefined;
      this.sentMealsRequest()
    } else {
      this.filteredCategory = category;
      this.sentMealsRequest(category.name)
    }
  }

  addToCart(meal: Meal) {
    this.cartService.addToCart(meal);
    this.toastService.showSuccessToast("Cart", "Added item to cart")
  }

  onChangePage(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.sentMealsRequest();
    window.scrollTo({top: 0});
  }

  ngOnDestroy() {
    this.authSubscription?.unsubscribe()
  }
}


