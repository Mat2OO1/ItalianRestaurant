import {Component, OnDestroy, OnInit} from '@angular/core';
import {Meal} from "../../models/meal";
import {CartService} from "../../shared/cart.service";
import {DataStorageService} from "../../shared/data-storage.service";
import {AuthService} from "../../authentication/auth/auth.service";
import {Subscription} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {Category} from "../../models/category";
import {PageEvent} from "@angular/material/paginator";
import {SnackbarService} from "../../shared/sncakbar.service";
import {Role} from "../../authentication/auth/user.model";

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
  isUser = false;
  filteredCategory ?: Category;
  lang = "";


  authSubscription?: Subscription | null;

  constructor(private cartService: CartService,
              private dataStorageService: DataStorageService,
              private snackbarService: SnackbarService,
              private authService: AuthService,
              private activatedRoute: ActivatedRoute) {
    this.authSubscription = this.authService.user.subscribe(
      (user) => {
        this.isUser = user?.role === Role.USER;
      }
    );
    this.sentMealsRequest();
  }


  ngOnInit(): void {
    this.lang = localStorage.getItem('lang') || 'en';
    if (this.activatedRoute.snapshot.queryParams['payment'] === 'failed') {
      this.snackbarService.openSnackbarError('Payment failed. Order not placed');
    }
    if (this.activatedRoute.snapshot.queryParams['table']) {
      this.cartService.addTable(this.activatedRoute.snapshot.queryParams['table'])
      this.snackbarService.openSnackbarSuccess('You are ordering to table' + this.activatedRoute.snapshot.queryParams['table']);
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
            this.totalPages = res.totalPages;
            this.mealsNumber = res.totalElements;
            this.pageIndex = res.number;
            this.meals = res.content.map(meal => new Meal(
              meal.id,
              meal.name,
              meal.name_pl,
              meal.image,
              meal.description,
              meal.description_pl,
              meal.price,
              meal.mealCategory
            ));
          }
        );
    } else {
      this.dataStorageService.getMeals(this.pageIndex, this.size)
        .subscribe(
          (res) => {
            this.totalPages = res.totalPages;
            this.mealsNumber = res.totalElements;
            this.pageIndex = res.number;
            this.meals = res.content.map(meal => new Meal(
              meal.id,
              meal.name,
              meal.name_pl,
              meal.image,
              meal.description,
              meal.description_pl,
              meal.price,
              meal.mealCategory
            ));
          }
        );
    }
  }

  onCategoryChange(category: Category) {
    this.pageIndex = 0;
    if (this.filteredCategory !== undefined && this.filteredCategory.name === category.name) {
      this.filteredCategory = undefined;
      this.sentMealsRequest()
    } else {
      this.filteredCategory = category;
      this.sentMealsRequest(this.filteredCategory.name)
    }
  }

  addToCart(meal: Meal) {
    this.cartService.addToCart(meal);
    this.snackbarService.openSnackbarSuccess('Meal added to cart');
  }

  onChangePage(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.size = event.pageSize;
    this.sentMealsRequest(this.filteredCategory?.name);
    window.scrollTo({top: 0});
  }

  getCategoryName(category: Category): string {
    this.lang = localStorage.getItem('lang') || 'en'
    return this.lang === 'pl' ? category.name_pl || category.name : category.name || '';
  }

  getMealName(meal: Meal): string {
    this.lang = localStorage.getItem('lang') || 'en'
    return this.lang === 'pl' ? meal.name_pl || meal.name : meal.name || '';
  }

  getMealDescription(meal: Meal): string {
    this.lang = localStorage.getItem('lang') || 'en'
    return this.lang === 'pl' ? meal.description_pl || meal.description : meal.description || '';
  }

  ngOnDestroy() {
    this.authSubscription?.unsubscribe()
  }

  isLargeScreen() {
    const width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
    return width > 720;
  }
}


