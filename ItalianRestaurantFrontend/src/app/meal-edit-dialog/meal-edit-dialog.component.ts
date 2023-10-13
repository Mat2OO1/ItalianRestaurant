import {Component, Inject} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Meal} from "../models/meal";
import {DataStorageService} from "../shared/data-storage.service";
import {MealDto} from "../models/mealDto";

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
              @Inject(DIALOG_DATA) public data: { mode: string, category: string, meal?: Meal },
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
    if(this.data.meal !== undefined){
      this.dataStorageService.editMeal(
        new MealDto(
          this.mealForm.value['name'],
          this.mealForm.value['imgPath'],
          this.mealForm.value['description'],
          this.mealForm.value['price'],
          this.data.category), this.data.meal.id
      ).subscribe()
    }
    this.dialogRef.close();
  }

  closeDialogAndDelete(): void {
    if(this.data.meal !== undefined) {
      this.dataStorageService.deleteMeal(
        this.data.meal.id
      ).subscribe(
        (response) => {
          console.log(response)
        }
      )
    }
    this.dialogRef.close();
  }

  closeDialogAndAdd(): void {
    this.dataStorageService.addMeal(
      new MealDto(
        this.mealForm.value['name'],
        this.mealForm.value['imgPath'],
        this.mealForm.value['description'],
        this.mealForm.value['price'],
        this.data.category)
    ).subscribe()
    this.dialogRef.close();
  }

}
