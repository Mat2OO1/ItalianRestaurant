import {Component, Inject} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {Category, Meal} from "../models/meal";

@Component({
  selector: 'app-category-edit-dialog',
  templateUrl: './category-edit-dialog.component.html',
  styleUrls: ['./category-edit-dialog.component.css'],
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
export class CategoryEditDialogComponent {

  mealForm: FormGroup
  constructor(private dialogRef: MatDialogRef<CategoryEditDialogComponent>,
              @Inject(DIALOG_DATA) public data: {mode: string, category ?: Category},) {
    this.mealForm = new FormGroup({
      name: new FormControl(data.category !== undefined ? data.category.name : '', [Validators.required]),
      imgPath: new FormControl(data.category !== undefined ? data.category.imgPath : '', [Validators.required]),
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
    this.dialogRef.close();
  }

}
