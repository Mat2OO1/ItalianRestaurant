import {Component, Inject} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Meal} from "../models/meal";
import {MealDto} from "../models/mealDto";
import {priceValidator} from "../shared/validators";
import {DialogMode} from "../models/modal-mode";

@Component({
  selector: 'app-meal-edit-dialog',
  templateUrl: './meal-edit-dialog.component.html',
  styleUrls: ['./meal-edit-dialog.component.css'],
})
export class MealEditDialogComponent {
  mealForm: FormGroup
  selectedFile: File | null = null;
  protected readonly DialogMode = DialogMode;

  constructor(private dialogRef: MatDialogRef<MealEditDialogComponent>,
              @Inject(DIALOG_DATA) public data: { mode: string, category: string, meal?: Meal }) {
    this.mealForm = new FormGroup({
      name: new FormControl(data.meal !== undefined ? data.meal.name : '', [Validators.required]),
      description: new FormControl(data.meal !== undefined ? data.meal.description : '', [Validators.required]),
      price: new FormControl(data.meal !== undefined ? data.meal.price : '', [Validators.required, priceValidator()]),
    })
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  closeDialogAndUpdate(): void {
    if (this.mealForm.valid) {
      let result = {
        mealDto: new MealDto(
          this.mealForm.value['name'],
          this.mealForm.value['description'],
          this.mealForm.value['price'],
          this.data.category),
        id: this.data.meal?.id,
        file: this.selectedFile
      }
      this.dialogRef.close(result);
    }
  }

  closeDialogAndDelete(): void {
    this.dialogRef.close(this.data.meal?.id);
  }

  onFileUploaded(event: any): void {
    this.selectedFile = event.target.files[0] as File;
  }
}
