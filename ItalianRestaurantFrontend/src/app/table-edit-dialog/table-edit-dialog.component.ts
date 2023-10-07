import {Component, Inject, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Meal} from "../models/meal";
import {DataStorageService} from "../shared/data-storage.service";
import {DialogMode} from "../models/modal-mode";
import {Table} from "../models/table";

@Component({
  selector: 'app-table-edit-dialog',
  templateUrl: './table-edit-dialog.component.html',
  styleUrls: ['./table-edit-dialog.component.css']
})
export class TableEditDialogComponent {
  tableForm: FormGroup

  constructor(private dialogRef: MatDialogRef<TableEditDialogComponent>,
              @Inject(DIALOG_DATA) public data: { mode: DialogMode, table?: Table },
              private dataStorageService: DataStorageService) {

    this.tableForm = new FormGroup({
      number: new FormControl(this.data.table ? this.data.table.number : '', [Validators.required]),
      description: new FormControl(this.data.table ? this.data.table.seats : '', [Validators.required]),
      price: new FormControl(this.data.table ? this.data.table.status : '', [Validators.required]),
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

  protected readonly DialogMode = DialogMode;
}
