import {Component, Inject, OnInit} from '@angular/core';
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

export class MealEditDialogComponent implements OnInit {
  mealForm: FormGroup
  selectedFile: File | null = null;
  lang =""
  ngOnInit() {
    this.lang = localStorage.getItem('lang') || 'en'
  }
  constructor(private dialogRef: MatDialogRef<MealEditDialogComponent>,
              @Inject(DIALOG_DATA) public data: { mode: string, category: string, meal?: Meal },
              private dataStorageService: DataStorageService) {
    this.mealForm = new FormGroup({
      name: new FormControl(data.meal !== undefined ? data.meal.name : '', [Validators.required]),
      name_pl: new FormControl(data.meal !== undefined ? data.meal.name_pl : '', [Validators.required]),
      description: new FormControl(data.meal !== undefined ? data.meal.description : '', [Validators.required]),
      description_pl: new FormControl(data.meal !== undefined ? data.meal.description_pl : '', [Validators.required]),
      price: new FormControl(data.meal !== undefined ? data.meal.price : '', [Validators.required]),
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
          this.mealForm.value['name_pl'],
          this.mealForm.value['description'],
          this.mealForm.value['description_pl'],
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
