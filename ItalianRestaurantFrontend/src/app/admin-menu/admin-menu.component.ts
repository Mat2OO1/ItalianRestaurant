import {Component} from '@angular/core';
import {Meal} from "../models/meal";
import {FormControl, FormGroup} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {MealEditDialogComponent} from "../meal-edit-dialog/meal-edit-dialog.component";
import {CategoryEditDialogComponent} from "../category-edit-dialog/category-edit-dialog.component";
import {DataStorageService} from "../shared/data-storage.service";
import {CategoryDto} from "../models/categoryDto";
import {DialogMode} from "../models/modal-mode";
import {MealDto} from "../models/mealDto";
import {Category} from "../models/category";

@Component({
  selector: 'app-admin-menu',
  templateUrl: './admin-menu.component.html',
  styleUrls: ['./admin-menu.component.css'],
})
export class AdminMenuComponent {
  mealsByCategory: { [category: string]: Meal[] } = {};
  categories: CategoryDto[] = []
  areMealsLoaded = false;
  areCategoriesLoaded = false;
  lang = localStorage.getItem('lang') || 'en';
  protected readonly DialogMode = DialogMode;

  constructor(public dialog: MatDialog,
              private dataStorageService: DataStorageService) {
    this.getMeals()
    this.getCategories()
  }

  openDialog(mode: DialogMode, category: CategoryDto, meal ?: Meal) {
    const dialogRef = this.dialog.open(MealEditDialogComponent, {
      data: {mode: mode, meal: meal, category: category.name},
    });
    dialogRef.afterClosed().subscribe(result => {
      this.handleDialogResult(mode, result);
    });
  }

  openCategoryDialog(mode: DialogMode, category ?: CategoryDto) {
    const dialogRef = this.dialog.open(CategoryEditDialogComponent, {
      data: {mode: mode, category: category},
    });
    dialogRef.afterClosed().subscribe(result => {
      this.handleCategoryDialogResult(mode, result)
    });
  }


  getMeals() {
    this.dataStorageService.getMealsWithoutPagination()
      .subscribe(
        (response) => {
          this.mealsByCategory = {}
          response.content.forEach(meal => {
            if (this.mealsByCategory[meal.mealCategory.name]) {
              this.mealsByCategory[meal.mealCategory.name].push(meal)
            } else {
              this.mealsByCategory[meal.mealCategory.name] = [meal]
            }
          })
          this.areMealsLoaded = true;
        }
      )
  }

  getCategories() {
    this.dataStorageService.getCategories()
      .subscribe(
        (response) => {
          this.categories = response;
          this.areCategoriesLoaded = true;
        }
      )
  }
  getCategoryName(category: Category): string {
    this.lang = localStorage.getItem('lang') || 'en'
    return this.lang === 'pl' ? category.name_pl || category.name : category.name || '';
  }
  getMealName(meal: any): string {
    this.lang = localStorage.getItem('lang') || 'en'
    return this.lang === 'pl' ? meal.name_pl || meal.name : meal.name || '';
  }

  getMealDescription(meal: any): string {
    this.lang = localStorage.getItem('lang') || 'en'
    return this.lang === 'pl' ? meal.description_pl || meal.description : meal.description || '';
  }

  private handleDialogResult(mode: DialogMode,
                             result: { mealDto: MealDto, id: number | undefined, file: File | null } | number) {
    if (result) {
      if (mode === DialogMode.ADD && typeof result === 'object') {
        this.dataStorageService.addMeal(result.mealDto, result.file!).subscribe(() => {
          this.getCategories();
          this.getMeals();
        });
      } else if (mode === DialogMode.EDIT && typeof result === 'object') {
        this.dataStorageService.editMeal(result.mealDto, result.id!, result.file!).subscribe(() => {
          this.getCategories();
          this.getMeals();
        });
      } else if (mode === DialogMode.DELETE && typeof result === 'number') {
        this.dataStorageService.deleteMeal(result).subscribe(() => {
          this.getCategories();
          this.getMeals();
        });
      }
    }
  }

  private handleCategoryDialogResult(mode: DialogMode,
                                     result: { name: string, name_pl: string, id: number | undefined } | number) {
    if (result) {
      if (mode === DialogMode.ADD && typeof result === 'object') {
        this.dataStorageService.addCategory(result.name, result.name_pl).subscribe(() => {
          this.getCategories();
          this.getMeals();
        });
      } else if (mode === DialogMode.EDIT && typeof result === 'object') {
        this.dataStorageService.editCategory(result.name, result.name_pl, result.id!).subscribe(() => {
          this.getCategories();
          this.getMeals();
        });
      } else if (mode === DialogMode.DELETE && typeof result === 'number') {
        this.dataStorageService.deleteCategory(result).subscribe(() => {
          this.getCategories();
          this.getMeals();
        });
      }
    }
  }
}
