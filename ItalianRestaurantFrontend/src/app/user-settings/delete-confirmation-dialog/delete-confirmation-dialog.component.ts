import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-delete-confirmation-dialog',
  templateUrl: './delete-confirmation-dialog.component.html',
  styleUrls: ['./delete-confirmation-dialog.component.css']
})
export class DeleteConfirmationDialogComponent {

  passwordForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<DeleteConfirmationDialogComponent>) {

    this.passwordForm = new FormGroup({
      currentPassword: new FormControl('', [Validators.required]),
    })
  }

  closeDialog() {
    this.dialogRef.close({result: false});
  }


  closeDialogAndDelete() {
    if(this.passwordForm.invalid) return;
    this.dialogRef.close({
      delete: true,
      password: this.passwordForm.value['currentPassword']});
  }
}
