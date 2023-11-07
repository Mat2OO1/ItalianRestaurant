import {Component, Inject} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DialogMode} from "../models/modal-mode";
import {Table} from "../models/table";

@Component({
  selector: 'app-table-edit-dialog',
  templateUrl: './table-edit-dialog.component.html',
  styleUrls: ['./table-edit-dialog.component.css']
})
export class TableEditDialogComponent {
  protected readonly DialogMode = DialogMode;
  tableForm: FormGroup

  constructor(private dialogRef: MatDialogRef<TableEditDialogComponent>,
              @Inject(DIALOG_DATA) public data: { mode: DialogMode, table?: Table }) {

    this.tableForm = new FormGroup({
      number: new FormControl(this.data.table ? this.data.table.number : '', [Validators.required]),
      seats: new FormControl(this.data.table ? this.data.table.seats : '', [Validators.required]),
    })
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  closeDialogAndUpdate(): void {
    if (this.tableForm.valid) {
      let result = this.tableForm.value;
      result.id = this.data.table?.id;
      this.dialogRef.close(result);
    }
  }

  closeDialogAndDelete(): void {
    this.dialogRef.close(this.data.table?.id);
  }
}
