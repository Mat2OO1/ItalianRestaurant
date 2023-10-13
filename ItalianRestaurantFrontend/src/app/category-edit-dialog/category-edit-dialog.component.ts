import {Component, Inject} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {CategoryDto} from "../models/categoryDto";
import {DataStorageService} from "../shared/data-storage.service";
import {Category} from "../models/category";

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
              @Inject(DIALOG_DATA) public data: {mode: string, category ?: Category},
              private dataStorageService: DataStorageService) {
    this.categoryForm = new FormGroup({
      name: new FormControl(data.category !== undefined ? data.category.name : '', [Validators.required]),
    })
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  closeDialogAndEdit(): void {
    if(this.data.category !== undefined){
      this.selectedFile!.name
      this.dataStorageService.editCategory(this.categoryForm.value['name'], this.selectedFile!, this.data.category.id)
        .subscribe()
    }
    this.dialogRef.close();
  }

  closeDialogAndDelete(): void {
    if(this.data.category !== undefined){
      this.dataStorageService.deleteCategory(this.data.category.id)
        .subscribe()
    }
    this.dialogRef.close();
  }

  closeDialogAndAdd(): void {
    console.log(`Selected file: ${this.selectedFile!.name}`);
    this.dataStorageService.addCategory(this.categoryForm.value['name'], this.selectedFile!)
      .subscribe()
    this.dialogRef.close();
  }

  onFileUploaded(event: any): void {
    this.selectedFile = event.target.files[0] as File;
  }

}
