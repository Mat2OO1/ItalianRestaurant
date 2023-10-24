import {Component, Inject} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {DataStorageService} from "../shared/data-storage.service";
import {Category} from "../models/category";
import {MealDto} from "../models/mealDto";

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

  categoryForm: FormGroup
  selectedFile: File | null = null;


  constructor(private dialogRef: MatDialogRef<CategoryEditDialogComponent>,
              @Inject(DIALOG_DATA) public data: { mode: string, category?: Category },
              private dataStorageService: DataStorageService) {
    this.categoryForm = new FormGroup({
      name: new FormControl(data.category !== undefined ? data.category.name : '', [Validators.required]),
    })
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  closeDialogAndUpdate(): void {
    if (this.categoryForm.valid) {
      let result = {
        name: this.categoryForm.value['name'],
        file: this.selectedFile!,
        id: this.data.category?.id
      }
      this.dialogRef.close(result);
    }
  }

  closeDialogAndDelete(): void {
    this.dialogRef.close(this.data.category?.id);
  }

  onFileUploaded(event: any): void {
    this.selectedFile = event.target.files[0] as File;
  }

}
