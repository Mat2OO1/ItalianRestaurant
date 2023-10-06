import {Component, Inject} from '@angular/core';
import {MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Category, Meal} from "../models/meal";
import {DataStorageService} from "../shared/data-storage.service";

@Component({
  selector: 'app-meal-edit-dialog',
  templateUrl: './meal-edit-dialog.component.html',
  styleUrls: ['./meal-edit-dialog.component.css'],
  styles: [`
    :host {
      display: block;
      width: 50vw;
      background: #fff;
      border-radius: 8px;
      padding: 16px;
    }
    :host * {
      color: black;
    }
  `]
})
export class MealEditDialogComponent {
  mealForm: FormGroup
  constructor(private dialogRef: MatDialogRef<MealEditDialogComponent>,
              @Inject(DIALOG_DATA) public data: {mode: string, meal ?: Meal},
              private dataStorageService: DataStorageService) {
    this.mealForm = new FormGroup({
      name: new FormControl(data.meal !== undefined ? data.meal.name : '', [Validators.required]),
      description: new FormControl(data.meal !== undefined ? data.meal.description : '', [Validators.required]),
      price: new FormControl(data.meal !== undefined ? data.meal.price : '', [Validators.required]),
      imgPath: new FormControl(data.meal !== undefined ? data.meal.imgPath : '', [Validators.required]),
    })
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  closeDialogAndEdit(): void {
    this.dialogRef.close();
  }

  closeDialogAndDelete(): void {
    this.dialogRef.close();
  }

  closeDialogAndAdd(): void {
    if(this.data.meal !== undefined){
      this.dataStorageService.addMeal(
        new Meal(this.data.meal.id, this.data.meal.name, this.data.meal.imgPath, this.data.meal.description, this.data.meal.price, this.data.meal.mealCategory)
      ).subscribe(
        (res) => {
          
        }
      )
    }
    this.dialogRef.close();
  }

}
