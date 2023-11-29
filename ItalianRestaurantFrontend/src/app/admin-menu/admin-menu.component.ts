import {Component} from '@angular/core';
import {Meal} from "../models/meal";
import {MatDialog} from "@angular/material/dialog";
import {MatExpansionModule} from '@angular/material/expansion';
import {MealEditDialogComponent} from "../meal-edit-dialog/meal-edit-dialog.component";
import {CategoryEditDialogComponent} from "../category-edit-dialog/category-edit-dialog.component";
import {DataStorageService} from "../shared/data-storage.service";
import {CategoryDto} from "../models/categoryDto";
import {DialogMode} from "../models/modal-mode";
import {MealDto} from "../models/mealDto";
import {Category} from "../models/category";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-admin-menu',
  templateUrl: './admin-menu.component.html',
  styleUrls: ['./admin-menu.component.css'],
})
export class AdminMenuComponent {
  categories?: CategoryDto[];
  lang = localStorage.getItem('lang') || 'en';
  clickButton = false;
  protected readonly DialogMode = DialogMode;

  displayedColumns: string[] = ['image', 'name_menu', 'menu_description', 'price', 'edit', 'delete'];
  dataSources?: {[category: string]: MatTableDataSource<Meal>};
  processing = false;

  constructor(public dialog: MatDialog,
              private dataStorageService: DataStorageService) {
    this.getMealsData()
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
    this.clickButton = true;
    const dialogRef = this.dialog.open(CategoryEditDialogComponent, {
      data: {mode: mode, category: category},
    });
    dialogRef.afterClosed().subscribe(result => {
      this.handleCategoryDialogResult(mode, result)
    });
  }


  getMealsData() {
    this.dataStorageService.getCategories()
      .subscribe(
        (response) => {
          this.categories = response;
          this.dataStorageService.getMealsWithoutPagination()
            .subscribe(
              (response) => {
                this.dataSources = {};
                response.forEach(meal => {
                  if (this.dataSources[meal.mealCategory.name]) {
                    this.dataSources[meal.mealCategory.name].data.push(meal);
                  } else {
                    this.dataSources[meal.mealCategory.name] = new MatTableDataSource();
                    this.dataSources[meal.mealCategory.name].data = [meal];
                  }
                })
                this.processing = false;
              })
        })
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
    this.processing = true;
    if (result) {
      if (mode === DialogMode.ADD && typeof result === 'object') {
        this.dataStorageService.addMeal(result.mealDto, result.file!).subscribe(() => {
          this.getMealsData();
        });
      } else if (mode === DialogMode.EDIT && typeof result === 'object') {
        this.dataStorageService.editMeal(result.mealDto, result.id!, result.file!).subscribe(() => {
          this.getMealsData();
        });
      } else if (mode === DialogMode.DELETE && typeof result === 'number') {
        this.dataStorageService.deleteMeal(result).subscribe(() => {
          this.getMealsData();
        });
      }
    }
  }

  private handleCategoryDialogResult(mode: DialogMode,
                                     result: { name: string, name_pl: string, id: number | undefined } | number) {
    this.processing = true;
    if (result) {
      if (mode === DialogMode.ADD && typeof result === 'object') {
        this.dataStorageService.addCategory(result.name, result.name_pl).subscribe(() => {
          this.getMealsData();
        });
      } else if (mode === DialogMode.EDIT && typeof result === 'object') {
        this.dataStorageService.editCategory(result.name, result.name_pl, result.id!).subscribe(() => {
          this.getMealsData();
        });
      } else if (mode === DialogMode.DELETE && typeof result === 'number') {
        this.dataStorageService.deleteCategory(result).subscribe(() => {
          this.getMealsData();
        });
      }
    }
  }
}
