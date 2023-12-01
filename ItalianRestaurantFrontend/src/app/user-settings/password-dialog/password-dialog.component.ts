import {Component, Inject} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../authentication/auth/auth.service";
import {SnackbarService} from "../../shared/sncakbar.service";
import {TranslateService} from "@ngx-translate/core";
import {MatDialogRef} from "@angular/material/dialog";
import {DIALOG_DATA} from "@angular/cdk/dialog";
import {PasswordDto} from "../../models/passwordDto";

@Component({
  selector: 'app-password-modal',
  templateUrl: './password-dialog.component.html',
  styleUrls: ['./password-dialog.component.css']
})
export class PasswordDialogComponent {
  passwordForm: FormGroup;
  processing = false;
  email?: string;

  constructor(private authService: AuthService,
              private snackbarService: SnackbarService,
              private translate: TranslateService,
              private dialogRef: MatDialogRef<PasswordDialogComponent>,
              @Inject(DIALOG_DATA) public data: { email: string }) {
    this.email = data.email;
    this.passwordForm = new FormGroup({
      currentPassword: new FormControl('', [Validators.required]),
      newPassword: new FormControl('', [Validators.required, Validators.minLength(8), this.passwordValidator()]),
    })
  }
  passwordValidator() {
    return (control: FormControl): { [key: string]: any } | null => {
      const valid = /[^a-zA-Z0-9]/.test(control.value);
      return valid ? null : { 'specialCharacterRequired': true };
    };
  }
  onSubmit() {
    this.processing = true;
    if (this.passwordForm.invalid || !this.email) return;
    let passwordDto: PasswordDto = {
      currentPassword: this.passwordForm.value['currentPassword'],
      newPassword: this.passwordForm.value['newPassword'],
    }
    this.authService.updateUserPassword(passwordDto).subscribe(() => {
      this.processing = false;
      this.translate.get('password_updated').subscribe((message) => {
        this.snackbarService.openSnackbarSuccess(message);
      });
      this.dialogRef.close();
    }, error => {
      this.processing = false;
      this.passwordForm.reset();
      this.translate.get('invalid_password').subscribe((message) => {
        this.snackbarService.openSnackbarError(message);
      });
    })
  }

}
