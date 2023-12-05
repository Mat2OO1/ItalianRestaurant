import {Component, QueryList, ViewChildren} from '@angular/core';
import {Meal} from "../models/meal";
import {MatDialog} from "@angular/material/dialog";
import {MealEditDialogComponent} from "../meal-edit-dialog/meal-edit-dialog.component";
import {CategoryEditDialogComponent} from "../category-edit-dialog/category-edit-dialog.component";
import {DataStorageService} from "../shared/data-storage.service";
import {DialogMode} from "../models/modal-mode";
import {MealDto} from "../models/mealDto";
import {Category} from "../models/category";
import {MatTable, MatTableDataSource} from "@angular/material/table";
import {SnackbarService} from "../shared/sncakbar.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-admin-menu',
  templateUrl: './admin-menu.component.html',
  styleUrls: ['./admin-menu.component.css'],
})
export class AdminMenuComponent {
  categories?: Category[];
  lang = localStorage.getItem('lang') || 'en';
  protected readonly DialogMode = DialogMode;
  clickButton = false;
  displayedColumns: string[] = ['image', 'name_menu', 'menu_description', 'price', 'edit', 'delete'];
  dataSources: { [category: string]: MatTableDataSource<Meal> } = {};
  processing = false;
  isDataLoaded = false;
  @ViewChildren(MatTable) tables?: QueryList<MatTable<Meal>>;

  constructor(public dialog: MatDialog,
              private dataStorageService: DataStorageService,
              private snackBarService: SnackbarService,
              private translate: TranslateService) {
    this.getMealsData()
  }

  openDialog(mode: DialogMode, category: Category, meal ?: Meal) {
    const dialogRef = this.dialog.open(MealEditDialogComponent, {
      data: {mode: mode, meal: meal, category: category.name},
    });
    dialogRef.afterClosed().subscribe(result => {
      this.handleDialogResult(mode, result, category);
    });
  }

  openCategoryDialog(mode: DialogMode, category ?: Category) {
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
          this.categories.forEach(category => {
            this.dataSources![category.name] = new MatTableDataSource();
          });
          this.dataStorageService.getMealsWithoutPagination()
            .subscribe(
              (response) => {
                response.forEach(meal => {
                  this.dataSources[meal.mealCategory.name].data.push(meal);
                })
                this.renderRows()
                this.processing = false;
                this.isDataLoaded = true;
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
                             result: { mealDto: MealDto, id: number | undefined, file: File | null } | number,
                             category?: Category) {

    if (result) {
      this.processing = true;
      if (mode === DialogMode.ADD && typeof result === 'object') {
        this.dataStorageService.addMeal(result.mealDto, result.file!)
          .subscribe((meal) => {
            this.dataSources![category!.name].data.push(meal);
            this.renderRows()
            this.processing = false;
          }, error => {
            if (error.status === 400) {
              this.translate.get('meal_already_exists').subscribe((message: string) => {
                this.snackBarService.openSnackbarError(message);
              })
            }
            this.renderRows()
            this.processing = false;
          });
      } else if (mode === DialogMode.EDIT && typeof result === 'object') {
        this.dataStorageService.editMeal(result.mealDto, result.id!, result.file!).subscribe((meal) => {
          this.dataSources![category!.name].data = this.dataSources![category!.name].data.map((m) => {
            if (m.id === meal.id) {
              return meal;
            }
            return m;
          });
          this.renderRows()
          this.processing = false;
        });
      } else if (mode === DialogMode.DELETE && typeof result === 'number') {
        this.dataStorageService.deleteMeal(result).subscribe(() => {
          this.dataSources![category!.name].data = this.dataSources![category!.name].data.filter((m) => m.id !== result);
          this.renderRows()
          this.processing = false;
        });
      }
    }
  }

  private handleCategoryDialogResult(mode: DialogMode,
                                     result: { name: string, name_pl: string, id: number | undefined } | number) {
    if (result) {
      this.processing = true;
      if (mode === DialogMode.ADD && typeof result === 'object') {
        this.dataStorageService.addCategory(result.name, result.name_pl)
          .subscribe((category) => {
            this.categories!.push(category);
            this.dataSources![category.name] = new MatTableDataSource();
            this.renderRows()
            this.processing = false;
          }, error => {
            if (error.status === 400) {
              this.translate.get('category_already_exists').subscribe((message: string) => {
                this.snackBarService.openSnackbarError(message);
              })
            }
            this.renderRows()
            this.processing = false;
          });
      } else if (mode === DialogMode.EDIT && typeof result === 'object') {
        this.dataStorageService.editCategory(result.name, result.name_pl, result.id!).subscribe((category) => {
          this.categories = this.categories!.map((c) => {
            if (c.id === category.id) {
              return category;
            }
            return c;
          });
          this.renderRows()
          this.processing = false;
        });
      } else if (mode === DialogMode.DELETE && typeof result === 'number') {
        this.dataStorageService.deleteCategory(result).subscribe(() => {
          delete this.dataSources![this.categories!.find((c) => c.id === result)!.name];
          this.categories = this.categories!.filter((c) => c.id !== result);
          this.renderRows()
          this.processing = false;
        });
      }
    }
  }

  renderRows() {
    if (!this.tables) return;
    this.tables.forEach(table => {
      table.renderRows();
    });
  }
}
