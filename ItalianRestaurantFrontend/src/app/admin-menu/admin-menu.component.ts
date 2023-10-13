import { Component } from '@angular/core';
import {Meal} from "../models/meal";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {MealEditDialogComponent} from "../meal-edit-dialog/meal-edit-dialog.component";
import {CategoryEditDialogComponent} from "../category-edit-dialog/category-edit-dialog.component";
import {DataStorageService} from "../shared/data-storage.service";
import {CategoryDto} from "../models/categoryDto";

@Component({
  selector: 'app-admin-menu',
  templateUrl: './admin-menu.component.html',
  styleUrls: ['./admin-menu.component.css'],
})
export class AdminMenuComponent {
    meals: Meal[] = []
    categories: CategoryDto[] = []
    mealForm: FormGroup
    areMealsLoaded = false;
    areCategoriesLoaded = false;

  constructor(public dialog: MatDialog,
              private dataStorageService: DataStorageService) {
    this.mealForm = new FormGroup({
      name: new FormControl(''),
      description: new FormControl(''),
      price: new FormControl(''),
    })
    this.getMeals()
    this.getCategories()
  }

  openDialog(mode: string, category: CategoryDto, meal ?: Meal) {
    const dialogRef = this.dialog.open(MealEditDialogComponent, {
      data: {mode: mode, meal: meal, category: category.name},
    });
    dialogRef.afterClosed().subscribe(result => {
      this.getMeals();
    });
  }

  openCategoryDialog(mode: string, category ?: CategoryDto){
    const dialogRef = this.dialog.open(CategoryEditDialogComponent, {
      data: {mode: mode, category: category},
    });
    dialogRef.afterClosed().subscribe(result => {
      this.getCategories();
    });
  }

  getMealsWithCategory(categoryName: string){
      return this.meals.filter(meal => meal.mealCategory.name === categoryName)
  }

  getMeals(){
    this.dataStorageService.getMealsWithoutPagination()
      .subscribe(
        (response) => {
          this.meals = response.content
          this.areCategoriesLoaded = true;
        }
      )
  }

  getCategories(){
    this.dataStorageService.getCategories()
      .subscribe(
        (response) => {
          this.categories = response;
          this.areMealsLoaded = true;
        }
      )
  }
}
