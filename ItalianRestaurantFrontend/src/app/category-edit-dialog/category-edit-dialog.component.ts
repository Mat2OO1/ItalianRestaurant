import {Component, Inject} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {Category} from "../models/category";
import {DialogMode} from "../models/modal-mode";

@Component({
  selector: 'app-category-edit-dialog',
  templateUrl: './category-edit-dialog.component.html',
  styleUrls: ['./category-edit-dialog.component.css']
})
export class CategoryEditDialogComponent {

  categoryForm: FormGroup
  lang = localStorage.getItem('lang') || 'en'
  protected readonly DialogMode = DialogMode;
  constructor(private dialogRef: MatDialogRef<CategoryEditDialogComponent>,
              @Inject(DIALOG_DATA) public data: { mode: string, category?: Category }
  ) {
    this.categoryForm = new FormGroup({
      name: new FormControl(data.category !== undefined ? data.category.name : '', [Validators.required]),
      name_pl: new FormControl(data.category !== undefined ? data.category.name_pl : '', [Validators.required]),
    })
  }
  closeDialog(): void {
    this.dialogRef.close();
  }

  closeDialogAndUpdate(): void {
    if (this.categoryForm.valid) {
      let result = {
        name: this.categoryForm.value['name'],
        name_pl: this.categoryForm.value['name_pl'],
        id: this.data.category?.id
      }
      this.dialogRef.close(result);
    }
  }

  closeDialogAndDelete(): void {
    this.dialogRef.close(this.data.category?.id);
  }
}
